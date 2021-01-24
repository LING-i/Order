package com.imooc.repository;

import com.imooc.dataonject.OrderMaster;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 订单表测试
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class OrderMasterRepositoryTest {

    @Autowired
   private  OrderMasterRepository orderMasterRepository;

    private  final String OPENID = "abc5";

    /**
     * 根据openId查询用户订单
     */
    @Test
    void findByBuyerOpenid() {
        PageRequest pageRequest = PageRequest.of(0,2);
        Page<OrderMaster> OrderListForOpenId = orderMasterRepository.findByBuyerOpenid("abc5", pageRequest);
        Assert.assertNotEquals(0,OrderListForOpenId.getTotalElements());
    }


    @Test
    void save(){
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId("a12345");
        orderMaster.setBuyerName("黎智兴");
        orderMaster.setBuyerPhone("18312854838");
        orderMaster.setBuyerOpenid(OPENID);
        orderMaster.setBuyerAddress("广东省梅州市兴宁市历陂镇");
        orderMaster.setOrderAmount(new BigDecimal(1000));
        OrderMaster result = orderMasterRepository.save(orderMaster);
        Assert.assertNotNull(result);
    }




}