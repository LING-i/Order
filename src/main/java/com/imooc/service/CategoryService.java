package com.imooc.service;

import com.imooc.dataonject.ProductCategory;

import java.util.List;
import java.util.Optional;
//类目
public interface CategoryService {

   Optional<ProductCategory> findOne(Integer categoryId);

   List<ProductCategory> findAll();

   List<ProductCategory>  findByCategoryTypeIn(List<Integer> categoryTypeList);

   ProductCategory save(ProductCategory productCategory);

}
