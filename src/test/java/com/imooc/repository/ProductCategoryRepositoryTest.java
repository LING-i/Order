package com.imooc.repository;

import com.imooc.dataonject.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryRepositoryTest {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Test
    public void findOneTest(){
        Optional<ProductCategory> productCategory = productCategoryRepository.findById(1);
        System.out.println(productCategory.toString());
    }

    @Test
    @Transactional //做完事就回滚,不污染数据库数据，只测试能不能通过
    public void saveOneTest(){
       ProductCategory productCategory = new ProductCategory("蛋糕",3);
       ProductCategory result = productCategoryRepository.save(productCategory);
       Assert.assertNotNull(result);//断言  值不为空，为空就抛出异常
    }

    @Test
    public void UpdateTest(){
        Optional<ProductCategory> productCategory = productCategoryRepository.findById(1);
        ProductCategory category = productCategory.get();//确定category不为空
        category.setCategoryType(1);
        productCategoryRepository.save(category);
    }

    @Test
    public void findByCategoryTypeInTest(){
        List<Integer> list = Arrays.asList(1, 2);
        List<ProductCategory> result = productCategoryRepository.findByCategoryTypeIn(list);

        Assert.assertNotEquals(0,result.size());  //size = 0 就抛出异常

    }









}