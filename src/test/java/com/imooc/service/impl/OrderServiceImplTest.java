package com.imooc.service.impl;

import com.imooc.dataonject.OrderDetail;
import com.imooc.dto.OrderDTO;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class OrderServiceImplTest {

    @Autowired
    private OrderServiceImpl orderService;

    private final String BUYER_OPENID="413363115";

    private final String ORDER_ID = "1611390182590354089";

    @Test
    void create() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName("黎智兴");
        orderDTO.setBuyerAddress("兴宁市");
        orderDTO.setBuyerPhone("18312854838");
        orderDTO.setBuyerOpenid(BUYER_OPENID);

        //购物车
        ArrayList<OrderDetail> orderDetailList = new ArrayList<>();
        OrderDetail o1 = new OrderDetail();
        o1.setProductId("12345678");
        o1.setProductQuantity(2);
        OrderDetail o2 = new OrderDetail();
        o2.setProductId("123457");
        o2.setProductQuantity(2);

        orderDetailList.add(o1);
        orderDetailList.add(o2);

        orderDTO.setOrderDetailList(orderDetailList);
        OrderDTO result = orderService.create(orderDTO);
        log.info("【创建订单】result={}",result);
        Assert.assertNotNull(result);
    }

    @Test
    void findById() {
        OrderDTO result = orderService.findById(ORDER_ID);
        log.info("【查询单个订单】 result={}",result);
        Assert.assertEquals(ORDER_ID,result.getOrderId());
    }

    @Test
    void findList() {
        String openId = "413363115";
        Pageable pageable = PageRequest.of(0,5);
        Page<OrderDTO> orderDTOPage = orderService.findList(openId, pageable);
        Assert.assertNotEquals(0,orderDTOPage.getTotalElements() );
    }

    @Test
    void cancel() {
        OrderDTO orderDTO = orderService.findById(ORDER_ID);
        OrderDTO cancel = orderService.cancel(orderDTO);
        Assert.assertEquals(cancel.getOrderStatus(),OrderStatusEnum.CANCEL.getCode());

    }

    @Test
    void finish() {
        OrderDTO orderDTO = orderService.findById(ORDER_ID);
        OrderDTO finishOrder = orderService.finish(orderDTO);
        Assert.assertEquals(OrderStatusEnum.FINISHED.getCode(),finishOrder.getOrderStatus());
    }

    @Test
    void paid() {
        OrderDTO orderDTO = orderService.findById(ORDER_ID);
        OrderDTO result = orderService.paid(orderDTO);
        Assert.assertEquals(PayStatusEnum.SUCCESS.getCode(),result.getPayStatus());
    }

    @Test
    void list(){

        PageRequest request = PageRequest.of(0,2);//构建页面
        Page<OrderDTO> orderDTOPage = orderService.findList(request);

        Assert.assertEquals(0,orderDTOPage.getTotalElements());
    }

}