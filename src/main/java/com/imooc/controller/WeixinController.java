package com.imooc.controller;

import com.imooc.VO.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/weixin")
@Slf4j
public class WeixinController {

    @GetMapping("/auth")
    public void auth(@RequestParam("code") String code,
                     @RequestParam("state") String state){
        log.info("进入auth方法。。。。");
                log.info("获取 code = {}",code);
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx297ad305157e338b&secret=00fde467df048493dc1739b4cf1ca2fb&code="+code+"&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        log.info("response ={}",response);

    }



}
