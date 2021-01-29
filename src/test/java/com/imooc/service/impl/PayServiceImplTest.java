package com.imooc.service.impl;

import com.imooc.dto.OrderDTO;
import com.imooc.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
class PayServiceImplTest {

    @Autowired
    private PayServiceImpl payService;

    @Autowired
    private OrderService orderService;

    @Test
    void create() {
        OrderDTO orderDTO = orderService.findById("1611390182590354089");
        payService.create(orderDTO);

    }

    @Test
    void cancel(){
        OrderDTO orderDTO = orderService.findById("1611756202546259271");
        payService.refund(orderDTO);
    }



}