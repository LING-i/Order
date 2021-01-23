package com.imooc.repository;

import com.imooc.dataonject.OrderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderMasterRepository extends JpaRepository<OrderMaster,String>,JpaSpecificationExecutor<String>{

    /**根据买家openid 分页来查询*/
    Page<OrderMaster> findByBuyerOpenid(String openid, Pageable pageable);


}
