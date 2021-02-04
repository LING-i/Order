package com.imooc.handler;

import com.imooc.config.ProjectUrlConfig;
import com.imooc.exception.SellerAuthorizeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class SellExceptionHandler {

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    //拦截登录异常
    @ExceptionHandler(value = SellerAuthorizeException.class)
    public ModelAndView handlerAuthorizeException(){
//        return new ModelAndView("redirect:"
//        .concat(projectUrlConfig.getWechatOpenAuthorize())
//        .concat("sell/wechat/qrAuthorize")
//        .concat("?returnUrl=")
//        .concat(projectUrlConfig.getSell()
//        .concat("/sell/seller/login")));

        return new ModelAndView("redirect:https://open.weixin.qq.com/connect/qrconnect?appid=wx6ad144e54af67d87&redirect_uri=http%3A%2F%2Fsell.springboot.cn%2Fsell%2Fqr%2FoTgZpwYwkJOW7c94gOyb0nKDe9bI&response_type=code&scope=snsapi_login&state=http%3A%2F%2Flzxstudy.natapp1.cc%2Fsell%2Fwechat%2FqrUserInfo");
    }


}
