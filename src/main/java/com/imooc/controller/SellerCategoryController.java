package com.imooc.controller;

import com.imooc.dataonject.ProductCategory;
import com.imooc.exception.SellException;
import com.imooc.form.CategoryForm;
import com.imooc.service.CategoryService;
import com.imooc.utils.KeyUtil;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 卖家类目 11-4
 */
@Controller
@RequestMapping("/seller/category")
public class SellerCategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public ModelAndView list(Map<String,Object> map){

        List<ProductCategory> categoryList = categoryService.findAll();
        map.put("categoryList", categoryList);

         return new ModelAndView("category/list",map);
    }


    /**
     * 展示类目
     * @return
     */
    @GetMapping("/index")
    public ModelAndView index(@RequestParam(value = "categoryId",required = false)Integer categoryId,
                              Map<String,Object> map){

        if(categoryId != null){
            Optional<ProductCategory> categoryOptional = categoryService.findOne(categoryId);
            ProductCategory category = categoryOptional.orElse(null);
            map.put("category",category);
        }

        return new ModelAndView("category/index",map);

    }


    @PostMapping("/save")
    public ModelAndView save(@Valid CategoryForm categoryForm,
                             BindingResult bindingResult,
                             Map<String,Object> map){

        if(bindingResult.hasErrors()){
            map.put("msg",bindingResult.getFieldError().getDefaultMessage());
            map.put("url","/sell/seller/category/index");
            return  new ModelAndView("common/error",map);
        }

        ProductCategory category = new ProductCategory();
        try{

            if(categoryForm.getCategoryId() != null){//修改类目
                Optional<ProductCategory> categoryOptional = categoryService.findOne(categoryForm.getCategoryId());
                category = categoryOptional.orElse(null);
            }
            BeanUtils.copyProperties(categoryForm,category);
            categoryService.save(category);
        }catch (SellException e){
            map.put("msg",e.getMessage());
            map.put("url","/sell/seller/category/index");
            return  new ModelAndView("common/error",map);
        }catch (Exception e){
            map.put("msg","类目Type已存在,请输入新的Type！  ");
            map.put("url","/sell/seller/category/index");
            return  new ModelAndView("common/error",map);
        }
        map.put("msg","类目保存成功！  ");
        map.put("url","/sell/seller/category/list");
        return new ModelAndView("common/success",map);
    }



}
