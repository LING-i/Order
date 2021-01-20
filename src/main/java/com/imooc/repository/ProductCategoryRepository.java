package com.imooc.repository;

import com.imooc.dataonject.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Integer>,JpaSpecificationExecutor<ProductCategory>{

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);



}
