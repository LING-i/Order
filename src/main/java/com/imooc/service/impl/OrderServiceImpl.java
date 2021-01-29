package com.imooc.service.impl;

import com.imooc.converter.OrderMaster2OrderDTOConverter;
import com.imooc.dataonject.OrderDetail;
import com.imooc.dataonject.OrderMaster;
import com.imooc.dataonject.ProductInfo;
import com.imooc.dto.CartDTO;
import com.imooc.dto.OrderDTO;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayStatusEnum;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.repository.OrderDetailRepository;
import com.imooc.repository.OrderMasterRepository;
import com.imooc.repository.ProductInfoRepository;
import com.imooc.service.OrderService;
import com.imooc.service.PayService;
import com.imooc.service.ProductService;
import com.imooc.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PayService payService;


    /**
     * OrderDTO
     * 创建订单
     */
    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {   //orderDTO中包含了所有购买的商品Id和数量
        //1.查询商品（数量、价格） 拿到用户想要购买商品的订单详情-去找数据库库存是否足够
        String orderId = KeyUtil.genUniqueKey();  //订单号
        BigDecimal orderAmount = new BigDecimal(0);//订单总金额
       for(OrderDetail orderDetail : orderDTO.getOrderDetailList()){
           //每个订单明细对应一个产品
           Optional<ProductInfo> optionalProductInfo = productInfoRepository.findById(orderDetail.getProductId());
           ProductInfo productInfo = optionalProductInfo.orElse(null);//拿到产品
           if(productInfo == null){//找不到订单对应的产品
               throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);//抛出产品不存在的异常
           }

           //2.计算订单总价
           orderAmount = productInfo.getProductPrice()
                   .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                   .add(orderAmount);

           //3.订单详情入数据库（orderDetail）
           orderDetail.setDetailId(KeyUtil.genUniqueKey());//订单明细的Id
           orderDetail.setOrderId(orderId);

           //OrderDetail中只存在 商品Id和数量
           BeanUtils.copyProperties(productInfo,orderDetail);
           orderDetailRepository.save(orderDetail);
       }

        //3. 写入订单数据库（orderMaster）
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO,orderMaster);//orderMaster也会被orderDTO中的null值覆盖
        orderMaster.setOrderAmount(orderAmount);
        //copyProperties中将orderMaster的OrderStatus和PayStatus覆盖 为 null
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterRepository.save(orderMaster);

        //4.下单成功-扣库存  CartDTO 只有Id和数量
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().map(e -> new CartDTO(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());

        //减库存
        productService.decreaseStock(cartDTOList);

        return orderDTO;
    }

    @Override
    public OrderDTO findById(String orderId) {

        Optional<OrderMaster> MasterOptional = orderMasterRepository.findById(orderId);
        OrderMaster orderMaster = MasterOptional.orElse(null);
        if(orderMaster == null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        //根据OrderMaster查询订单详情
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if(CollectionUtils.isEmpty(orderDetailList)){
              throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderDetailList(orderDetailList);
        BeanUtils.copyProperties(orderMaster,orderDTO);

        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {

        Page<OrderMaster> orderMasterPage = orderMasterRepository.findByBuyerOpenid(buyerOpenid, pageable);
        List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());

        return new PageImpl<OrderDTO>(orderDTOList,pageable,orderMasterPage.getTotalElements());

    }

    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {

        OrderMaster orderMaster = new OrderMaster();


        //1.判断订单状态 - 只有新创建状态的等待能被取消
       if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
           log.error("【取消订单】 订单状态不正确，orderId = {},orderStatus = {}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
           throw  new SellException(ResultEnum.ORDER_STATUS_ERROR);
       }
        //2.修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster updateResult  = orderMasterRepository.save(orderMaster);
        if(updateResult == null){
            log.error("【更新订单失败】，orderMaster = {}",orderMaster );
            throw  new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        //3.返回库存
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream()
                .map(e -> new CartDTO(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productService.increaseStock(cartDTOList);

        //4.如果已支付，需要退款
        if(orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())){
            //TODO
            payService.refund(orderDTO);//退款
        }

        return orderDTO;
    }


    @Override
    @Transactional
    public OrderDTO finish(OrderDTO orderDTO) {
        //1.判断订单状态
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【完结订单】 订单状态不正确，orderId = {},orderStatus = {}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //修改订单状态，订单状态为新创建 -- 允许修改
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if(updateResult == null){
                log.error("【完结订单】 更新失败，orderMaster = {}",orderMaster );
                throw  new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) {

        //判断订单状态 -- 新创建的订单，且为等待支付
        if(! orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【支付订单】 订单状态不正确,orderId = {},orderStatus = {}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        if(! orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())){
            log.error("【支付订单】 订单支付状态不正确，orderDTO = {}",orderDTO);
            throw  new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }
        //修改订单状态
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if(updateResult == null){
            log.error("【支付订单】 支付失败，orderMaster = {}",orderMaster );
            throw  new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(Pageable pageable)
    {

        Page<OrderMaster> masterPage = orderMasterRepository.findAll(pageable);
        //将MasterPage 转化为 OrderDTO
        List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(masterPage.getContent());

        return new PageImpl<OrderDTO>(orderDTOList,pageable,masterPage.getTotalElements());
    }


}
