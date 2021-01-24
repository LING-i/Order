package com.imooc.controller;

import com.imooc.VO.ProductInfoVO;
import com.imooc.VO.ProductVO;
import com.imooc.VO.ResultVO;
import com.imooc.dataonject.ProductCategory;
import com.imooc.dataonject.ProductInfo;
import com.imooc.service.CategoryService;
import com.imooc.service.ProductService;
import com.imooc.utils.ResultVOUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    private ResultVO list(){

        //1.查询所有上架的商品
        List<ProductInfo> productInfoList = productService.findUpAll();
        //2.查询商品的类目（一次性查询） - 根据上架的商品
//        List<Integer> categoryTypeList = new ArrayList<>();//上架商品的类目集合
//        for(ProductInfo productInfo : productInfoList){
//            categoryTypeList.add(productInfo.getCategoryType());
//        }


        //精简的lambda-java8  -->遍历商品得到类目的集合
        List<Integer> categoryTypeList = productInfoList.stream()
                .map(e -> e.getCategoryType())
                .collect(Collectors.toList());


        List<ProductCategory> productCategoryList = categoryService.findByCategoryTypeIn(categoryTypeList);
        //3.数据拼装

         List<ProductVO> productVOList = new ArrayList<>();//类目集合
        for(ProductCategory productCategory : productCategoryList){
            ProductVO productVO = new ProductVO();//商品的类目
            productVO.setCategoryType(productCategory.getCategoryType());
            productVO.setCategoryName(productCategory.getCategoryName());

            List<ProductInfoVO> productInfos = new ArrayList<>();//商品集合
            for(ProductInfo productInfo : productInfoList){
                if (productInfo.getCategoryType().equals(productCategory.getCategoryType()) ){
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo,productInfoVO);
                    productInfos.add(productInfoVO);
                }
            }
            productVO.setProductInfoVOList(productInfos);//商品集合->类目集合
            productVOList.add(productVO);//类目集合->result
        }

        return ResultVOUtil.success(productVOList);

    }



}
