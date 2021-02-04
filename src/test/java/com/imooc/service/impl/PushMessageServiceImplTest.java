package com.imooc.service.impl;

import com.imooc.dto.OrderDTO;
import com.imooc.service.OrderService;
import com.imooc.service.PushMessageService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class PushMessageServiceImplTest {

    @Autowired
    private PushMessageService pushMessageService;

    @Autowired
    private OrderService orderService;

    @Test
    void orderStatus() {

        String orderId = "1611390223228813325";
        OrderDTO orderDTO = orderService.findById(orderId);
        pushMessageService.orderStatus(orderDTO);//推送微信消息
        //正式的账号 需要在微信公众平台设置Ip白名单
    }
}