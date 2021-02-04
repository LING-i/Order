package com.imooc.repository;

import com.imooc.dataonject.SellerInfo;
import com.imooc.utils.KeyUtil;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class SellerInfoRepositoryTest {

    @Autowired
    private SellerInfoRepository sellerInfoRepository;

    @Test
    public void save(){
        SellerInfo sellerInfo = new SellerInfo();
        sellerInfo.setSellerId(KeyUtil.genUniqueKey());
        sellerInfo.setUsername("admin");
        sellerInfo.setPassword("admin");
        sellerInfo.setOpenid("abc");
        SellerInfo result = sellerInfoRepository.save(sellerInfo);
        Assert.assertNotNull(result);
    }


    @Test
    void findByOpenid() {

        SellerInfo sellerInfo = sellerInfoRepository.findByOpenid("abc");
        Assert.assertNotNull(sellerInfo);

    }
}