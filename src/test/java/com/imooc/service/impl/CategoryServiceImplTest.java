package com.imooc.service.impl;

import com.imooc.dataonject.ProductCategory;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class CategoryServiceImplTest {

    @Autowired
    private CategoryServiceImpl categoryServiceImpl;

    @Test
    void findOne() {
        /**
         * get()  null会报空指针异常
         * orElse（ new Product（））  有值去值，无值则取  new Product（）
         *
         */
        Optional<ProductCategory> one = categoryServiceImpl.findOne(9);
        System.out.println(one.orElse(new ProductCategory()).toString());
    }

    @Test
    void findAll() {
        System.out.println(categoryServiceImpl.findAll().toString());
    }

    @Test
    void findByCategoryTypeIn() {
        List<Integer> list = Arrays.asList(1, 2);
        List<ProductCategory> typeIn = categoryServiceImpl.findByCategoryTypeIn(list);
        System.out.println(typeIn.toString());
    }

    @Test
    void save() {
        ProductCategory productCategory = new ProductCategory("汽水",4);
        categoryServiceImpl.save(productCategory);
    }
}