package com.imooc.constant;

import lombok.Data;

/**
 * redis常量
 */
public interface RedisConstant {

     Integer EXPIRE = 7200;//2小时

     String TOKEN_PREFIX = "token_%s";//redis中的token的key以该字段开头

}
