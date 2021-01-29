package com.imooc.controller;

import com.imooc.dto.OrderDTO;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.service.OrderService;
import com.imooc.service.PayService;
import com.imooc.utils.JsonUtil;
import com.imooc.utils.MathUtil;
import com.lly835.bestpay.model.PayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Map;

@Controller
@RequestMapping("/pay")
@Slf4j
public class PayController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PayService payService;

    @GetMapping("/create")
    public ModelAndView create(@RequestParam("orderId")String orderId,
                               @RequestParam("returnUrl")String returnUrl,
                               Map<String,Object> map) throws UnsupportedEncodingException {

        //1.查询订单
        OrderDTO orderDTO = orderService.findById(orderId);
        if(orderDTO == null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        PayResponse payResponse = payService.create(orderDTO);
        map.put("payResponse",payResponse);
        map.put("returnUrl",returnUrl.startsWith("http://") ? returnUrl : 	URLEncoder.encode(returnUrl, "utf-8"));

        //2.发起支付
        return  new ModelAndView("pay/create",map);
    }


    /**
     * 微信异步通知
     */
    @PostMapping("/notify")
    public ModelAndView notify(@RequestBody String notifyData){
        PayResponse payResponse = payService.notify(notifyData);
        //通知微信订单状态已修改
        return new ModelAndView("pay/success");
    }


}
