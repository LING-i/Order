package com.imooc.enums;

import lombok.Getter;

@Getter
public enum OrderStatusEnum implements CodeEnum {

    NEW(0,"新订单"),
    FINISHED(1,"完成订单"),
    CANCEL(2,"已取消");

    private Integer code;
    private String message;

    OrderStatusEnum(Integer code,String message) {
        this.code = code;
        this.message = message;
    }

//
//    public static OrderStatusEnum getOrderStatusEnum(Integer code){
//        for(OrderStatusEnum each : OrderStatusEnum.values()){
//            if(each.getCode().equals(code)){
//                return  each;
//            }
//        }
//        return  null;
//    }


}
