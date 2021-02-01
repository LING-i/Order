package com.imooc.service.impl;

import com.imooc.dataonject.ProductInfo;
import com.imooc.dto.CartDTO;
import com.imooc.enums.ProductStatusEnum;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.repository.ProductInfoRepository;
import com.imooc.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductInfoRepository infoRepository;

    @Override
    public Optional<ProductInfo> findById(String productId) {
        return infoRepository.findById(productId);
    }

    @Override
    public List<ProductInfo> findUpAll() {
        return infoRepository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return infoRepository.findAll(pageable);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return infoRepository.save(productInfo);
    }

    @Override
    @Transactional
    public void increaseStock(List<CartDTO> cartDTOList) {
        for(CartDTO cartDTO : cartDTOList){
            //根据商品Id找到库存
            Optional<ProductInfo> infoOptional = infoRepository.findById(cartDTO.getProductId());
            ProductInfo productInfo = infoOptional.get();
            if(productInfo == null ){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            //进行加库存
            Integer result = productInfo.getProductStock() + cartDTO.getProductQuantity();
            productInfo.setProductStock(result);

            infoRepository.save(productInfo);
        }
    }

    @Override
    @Transactional
    public void decreaseStock(List<CartDTO> cartDTOList) {
        for(CartDTO cartDTO : cartDTOList){//cartDTOList是用户购买的商品的对象的集合，对象里属性只有Id和数量
            //根据商品Id找到库存
            Optional<ProductInfo> infoOptional = infoRepository.findById(cartDTO.getProductId());
            ProductInfo productInfo = infoOptional.orElse(null);
            if(productInfo == null ){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            //进行减库存
            Integer result = productInfo.getProductStock() - cartDTO.getProductQuantity();
            if(result < 0){
                throw new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
            }
            productInfo.setProductStock(result);

            infoRepository.save(productInfo);
        }
    }


    @Override
    public ProductInfo onSale(String productId) {
        //根据id查询ProductInfo
        Optional<ProductInfo> infoOptional = infoRepository.findById(productId);
        ProductInfo productInfo = infoOptional.orElse(null);
        if(productInfo == null){
            log.error("【更新商品信息】 商品不存在 productId={}",productId);
            throw  new SellException(ResultEnum.PRODUCT_NOT_EXIST);//抛出商品不存在异常
        }
        if(productInfo.getProductStatusEnum().getCode() == ProductStatusEnum.UP.getCode()){
            log.error("【更新商品信息】 商品已是 = {}",productInfo.getProductStatusEnum().getMessage());
            throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
        }
        //修改ProductInfo的状态
        productInfo.setProductStatus(ProductStatusEnum.UP.getCode());
        //保存product
        return  infoRepository.save(productInfo);
    }

    @Override
    public ProductInfo offSale(String productId) {
        //根据id查询ProductInfo
        Optional<ProductInfo> infoOptional = infoRepository.findById(productId);
        ProductInfo productInfo = infoOptional.orElse(null);
        if(productInfo == null){
            log.error("【更新商品信息】 商品不存在 productId={}",productId);
            throw  new SellException(ResultEnum.PRODUCT_NOT_EXIST);//抛出商品不存在异常
        }
        if(productInfo.getProductStatusEnum().getCode() == ProductStatusEnum.DOWN.getCode()){
            log.error("【更新商品信息】 商品已是 = {}",productInfo.getProductStatusEnum().getMessage());
            throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
        }
        //修改ProductInfo的状态
        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());
        //保存product
        return   infoRepository.save(productInfo);
    }

}
