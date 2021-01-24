package com.imooc.service;

import com.imooc.dataonject.OrderMaster;
import com.imooc.dto.OrderDTO;
import com.imooc.repository.OrderMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {


    /** 创建订单. */
    OrderDTO create(OrderDTO orderDTO);

    /** 查询一条订单. */
    OrderDTO findById(String orderId);

    /** 分页查询订单列表. */
    Page<OrderDTO> findList(String buyerOpenid, Pageable pageable);

    /**取消订单. */
    OrderDTO cancel(OrderDTO orderDTO);

    /**完结订单. */
    OrderDTO finish(OrderDTO orderDTO);

    /**支付订单. */
    OrderDTO paid(OrderDTO orderDTO);
}
