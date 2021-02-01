package com.imooc.controller;

import com.imooc.dto.OrderDTO;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;
import java.util.Map;


/**
 * 卖家端订单开发
 */
@Controller
@RequestMapping("/seller/order")
@Slf4j
public class SellerOrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 分页查询所有订单
     * @param page
     * @param size
     * @param map
     * @return
     */
    @GetMapping("/list")
    public ModelAndView list(@RequestParam(value = "page",defaultValue = "1") Integer page,
                     @RequestParam(value = "size",defaultValue = "5") Integer size,
                     Map<String,Object> map){
        //构造页面对象
        PageRequest request = PageRequest.of(page - 1, size);
        Page<OrderDTO> orderDTOPage = orderService.findList(request);
        map.put("orderDTOPage",orderDTOPage);
        map.put("currentPage",page);//当前页，制作前端当前页不可点的效果
        map.put("size",size);
        return new  ModelAndView("order/list",map);
    }


    /**
     * 取消订单
     */
    @GetMapping("/cancel")
    public ModelAndView cancel(@RequestParam("orderId") String orderId,
                               Map<String,Object> map){

        try{
            OrderDTO orderDTO = orderService.findById(orderId);
             orderService.cancel(orderDTO);

        }catch (SellException e){
            log.error("【卖家端取消订单】 发生异常 {}",e);
            map.put("msg",e.getMessage());
            map.put("url","/sell/seller/order/list");//错误提示后跳转的页面
            return new ModelAndView("common/error",map);//错误页面
        }

        map.put("msg", ResultEnum.ORDER_CANCEL_SUCCESS.getMessage());
        map.put("url","/sell/seller/order/list");//成功提示后跳转的页面

        return new ModelAndView("common/success",map);
    }


    /**
     * 订单详情
     */
    @RequestMapping("/detail")
    public ModelAndView detail(@RequestParam("orderId") String orderId,
                               Map<String,Object> map){

        OrderDTO orderDTO = new OrderDTO();
        try{
            orderDTO =  orderService.findById(orderId);
        }catch (SellException e){
            log.error("【卖家端查询订单详情】发生异常={}",e);
            map.put("msg",e.getMessage());
            map.put("url","/sell/seller/order/list");
            return new ModelAndView("common/error",map);//错误页面
        }

        map.put("orderDTO",orderDTO);

        return new ModelAndView("/order/detail",map);
    }


    /**
     * 完结订单
     */
    @RequestMapping("/finish")
    public ModelAndView finish(@RequestParam("orderId") String orderId,
                               Map<String,Object> map){

        OrderDTO orderDTO =  new OrderDTO();
        try{
            orderDTO =  orderService.findById(orderId);
            OrderDTO finish = orderService.finish(orderDTO);
        }catch (SellException e){
            log.error("【卖家端完结订单】 发生异常{}",e);
            map.put("msg",e.getMessage());
            map.put("url","/sell/seller/order/list");
            return  new ModelAndView("common/error",map);
        }

        map.put("msg", ResultEnum.ORDER_FINISH_SUCCESS.getMessage());
        map.put("url","/sell/seller/order/list");//成功提示后跳转的页面

        return new ModelAndView("common/success",map);
    }





}
