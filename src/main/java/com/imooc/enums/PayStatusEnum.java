package com.imooc.enums;

import lombok.Getter;

@Getter
public enum  PayStatusEnum implements CodeEnum {


    WAIT(0,"等待支付"),
    SUCCESS(1,"支付成功");


    private Integer code;
    private String message;


    PayStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

//    public static PayStatusEnum getPayStatusEnum(Integer code){
//        for(PayStatusEnum each : PayStatusEnum.values()){
//            if(each.getCode().equals(code)){
//                return each;
//            }
//        }
//        return null;
//    }


}
