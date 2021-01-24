package com.imooc.repository;

import com.imooc.dataonject.OrderDetail;
import org.aspectj.weaver.ast.Or;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class OrderDetailRepositoryTest {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Test
    void findByOrderId() {
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId("a12345");
        Assert.assertNotEquals(0,orderDetailList.size());
    }

    @Test
    void save(){
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId("a12345");
        orderDetail.setDetailId("x124");
        orderDetail.setProductId("12345678");
        orderDetail.setProductName("贵州茅台");
        orderDetail.setProductPrice(new BigDecimal("3200"));
        orderDetail.setProductQuantity(10);
        OrderDetail result = orderDetailRepository.save(orderDetail);
        Assert.assertNotNull(result);
    }


}