package com.imooc.repository;

import com.imooc.dataonject.OrderDetail;
import com.imooc.dataonject.OrderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,String>, JpaSpecificationExecutor<String> {

    /**根据订单Id查询订单详情*/
    List<OrderDetail> findByOrderId(String orderId);


}
