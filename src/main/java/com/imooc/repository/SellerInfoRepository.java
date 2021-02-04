package com.imooc.repository;

import com.imooc.dataonject.SellerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SellerInfoRepository extends JpaRepository<SellerInfo,String>, JpaSpecificationExecutor<String> {

    SellerInfo findByOpenid(String openid);


}
