package com.imooc.service.impl;

import com.imooc.dto.OrderDTO;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.service.OrderService;
import com.imooc.service.PayService;
import com.imooc.utils.JsonUtil;
import com.imooc.utils.MathUtil;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundRequest;
import com.lly835.bestpay.model.RefundResponse;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PayServiceImpl implements PayService {

    @Autowired
    private BestPayServiceImpl bestPayService;

    @Autowired
    private OrderService orderService;

    @Override
    public PayResponse create(OrderDTO orderDTO) {
        PayRequest payRequest = new PayRequest();
        payRequest.setOrderId(orderDTO.getOrderId());
        payRequest.setOpenid(orderDTO.getBuyerOpenid());
        payRequest.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        payRequest.setOrderName("微信点餐订单");
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_MP);
        log.info("【微信支付】 request = {}", JsonUtil.toJson(payRequest));

        PayResponse response = bestPayService.pay(payRequest);
        log.info("【微信支付】response={}",JsonUtil.toJson(response));

        return response;

    }

    @Override
    public PayResponse notify(String notifyData) {
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
        log.info("【微信支付】 异步通知，payResponse={}",JsonUtil.toJson(payResponse));

        //接收到支付的异步通知 -> 修改订单的支付状态
        //1.根据OrderId找到订单
        OrderDTO orderDTO = orderService.findById(payResponse.getOrderId());
        if(orderDTO == null){
            log.error("【微信支付】 异步通知, 订单不存在,orderId={}",payResponse.getOrderId());
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        //2.判断金额是否一致
        if(!MathUtil.equals(payResponse.getOrderAmount(),orderDTO.getOrderAmount().doubleValue())){
            log.error("【微信支付】 异步通知,订单金额不一致 orderId={},微信通知金额={},系统金额={}",payResponse.getOrderId(),payResponse.getOrderAmount(),orderDTO.getOrderAmount());
            throw  new SellException(ResultEnum.WXPAY_NOTIFY_MONEY_VERIFY_ERROR);
        }

        //3.修改订单状态
        orderService.paid(orderDTO);

        return payResponse;
    }


    /**
     * 微信退款
     * @param orderDTO
     * @return RefundResponse
     */
    @Override
    public RefundResponse refund(OrderDTO orderDTO) {

        RefundRequest request = new RefundRequest();
        request.setOrderId(orderDTO.getOrderId());
        request.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        request.setPayTypeEnum(BestPayTypeEnum.WXPAY_MP);
        log.info("【微信退款】 RefundRequest={}",JsonUtil.toJson(request));

        RefundResponse response = bestPayService.refund(request);
        log.info("【微信退款】 RefundResponse={}",JsonUtil.toJson(response));

        return response;
    }



}
