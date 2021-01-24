package com.imooc.dto;

import com.imooc.dataonject.OrderDetail;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
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
    private Date createTime;

    private Date updateTime;

    private List<OrderDetail> orderDetailList;




}
