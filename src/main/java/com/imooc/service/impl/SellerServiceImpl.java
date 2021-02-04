package com.imooc.service.impl;

import com.imooc.dataonject.SellerInfo;
import com.imooc.repository.SellerInfoRepository;
import com.imooc.service.SellerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SellerServiceImpl implements SellerService {


    @Autowired
    private SellerInfoRepository sellerInfoRepository;

    @Override
    public SellerInfo findSellerInfoByOpenid(String openid) {
        return  sellerInfoRepository.findByOpenid(openid);
    }



}
