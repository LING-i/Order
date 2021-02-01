package com.imooc.service;


import com.imooc.dataonject.ProductInfo;
import com.imooc.dto.CartDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 商品的Service
 */
public interface ProductService {

    Optional<ProductInfo> findById(String productId);

    /**显示所有在架商品*/
    List<ProductInfo> findUpAll();

    /**分页查询所有商品,返回page对象*/
    Page<ProductInfo> findAll(Pageable pageable);


    //保存商品对象为什么返回ProductInfo？
    ProductInfo save(ProductInfo productInfo);

    //加库存
    void increaseStock(List<CartDTO> cartDTOList);

    //减库存
    void decreaseStock(List<CartDTO> cartDTOList);

    //上架
    ProductInfo onSale(String productId);


    //下架
    ProductInfo offSale(String productId);


}
