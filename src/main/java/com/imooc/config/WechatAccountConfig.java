package com.imooc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;

@Data
@Component
@ConfigurationProperties(prefix = "wechat")
//@PropertySource(value = "classPath:wechat.yml")
public class WechatAccountConfig {

        private String mpAppId;

        private String mpAppSecret;


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

}
