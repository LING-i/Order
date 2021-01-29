package com.imooc.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 进行表单验证
 */
@Data
public class OrderForm {

    /**
     * 买家姓名
     */
    @NotNull(message = "姓名必填")
    private String name;

    /**
     * 买家手机号
     */
    @NotNull(message = "手机号码必填")
    private String phone;

    @NotNull(message = "地址必填")
    private String address;

    @NotNull(message = "openid必填")
    private String openid;

    @NotNull(message = "购物车不能为空")
    private String items;


}
