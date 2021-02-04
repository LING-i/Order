package com.imooc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "wechat")
//@PropertySource(value = "classPath:wechat.yml")
public class WechatAccountConfig {

        /**公众平台Id*/
        private String mpAppId;

        /**公众平台密钥*/
        private String mpAppSecret;

        /**开放平台Id*/
        private String openAppId;

        /**开放平台密钥*/
        private String openAppSecret;

        /**
         * 商户号
         */
        private String mchId;

        /**
         * 商户密钥
         */
        private String mchKey;

        /**
         * 商户证书路径
         */
        private String keyPath;

        /**
         * 证书内容
         */
        private SSLContext sslContext;

        /**
         * 异步URl
         */
        private String notifyUrl;


        private Map<String,String> templateId;

}
