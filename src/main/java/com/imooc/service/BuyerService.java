package com.imooc.service;

import com.imooc.dto.OrderDTO;
import org.springframework.data.domain.jaxb.SpringDataJaxb;

public interface BuyerService {

    OrderDTO findOrderOne(String openid,String orderId);

    OrderDTO cancelOrder(String openid,String OrderId);
}
