package com.imooc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.imooc.dataonject.OrderDetail;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayStatusEnum;
import com.imooc.utils.serializer.Date2LongSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
//@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)  弃用
//@JsonInclude(JsonInclude.Include.NON_NULL) √
public class OrderDTO {


    private String orderId;

    /** 买家名字. */
    private String buyerName;

    /** 买家手机号. */
    private String buyerPhone;

    /** 买家地址. */
    private String buyerAddress;

    /** 买家微信Openid. */
    private String buyerOpenid;

    /** 订单总金额. */
    private BigDecimal orderAmount;

    /**订单状态，默认为0 新下单*/
    private Integer orderStatus;

    /**支付状态，默认为0，未支付*/
    private Integer payStatus;

    /**创建时间*/
    @JsonSerialize(using = Date2LongSerializer.class )  //为了返回格式
    private Date createTime;

    @JsonSerialize(using = Date2LongSerializer.class )
    private Date updateTime;


    private List<OrderDetail> orderDetailList;




}
