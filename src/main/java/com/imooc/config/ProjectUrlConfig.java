package com.imooc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "project-url")
@Data
public class ProjectUrlConfig {

    private  String wechatMpAuthorize;

    private String  wechatOpenAuthorize;

    private String  sell;


}
