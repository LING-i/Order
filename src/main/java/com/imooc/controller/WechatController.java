package com.imooc.controller;

import com.imooc.config.ProjectUrlConfig;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
@RequestMapping("/wechat")
@Slf4j
public class WechatController {

        @Autowired
        private WxMpService wxMpService;

        @Autowired
        private WxMpService wxOpenService;

        @Autowired
        private ProjectUrlConfig projectUrlConfig;

        @GetMapping("/authorize")
        public String  authorize(@RequestParam("returnUrl") String returnUrl) throws UnsupportedEncodingException {
//         String url = "http://lzxstudy.natapp1.cc/sell/wechat/userInfo";
           String url =  projectUrlConfig.getWechatMpAuthorize() +  "/sell/wechat/userInfo";
         //拼凑获取code的url
        String redirectUrl =  wxMpService.getOAuth2Service().buildAuthorizationUrl(url, WxConsts.OAuth2Scope.SNSAPI_USERINFO, URLEncoder.encode(returnUrl,"UTF-8"));
        return "redirect:" + redirectUrl;
        }

        @GetMapping("/userInfo")
        public String userInfo(@RequestParam("code") String code,
                             @RequestParam("state") String returnUrl){
                WxOAuth2AccessToken accessToken = new WxOAuth2AccessToken();
                try {  //利用code 获取token_access
                        accessToken = wxMpService.getOAuth2Service().getAccessToken(code);
                } catch (WxErrorException e) {
                       log.error("【微信网页授权】{}",e);
                       throw new SellException(ResultEnum.WECHAT_MP_ERROR.getCode(),e.getError().getErrorMsg());
                }

                String openId = accessToken.getOpenId();
                return "redirect:" + returnUrl + "?openid=" + openId;

        }


        @GetMapping("/qrAuthorize")
        public String qrAuthorize(@RequestParam("returnUrl") String returnUrl) throws UnsupportedEncodingException {

//         String url = "http://lzxstudy.natapp1.cc/sell/wechat/qrUserInfo";
         String url = projectUrlConfig.getWechatOpenAuthorize() + "/sell/wechat/qrUserInfo";
         //拼凑获取code的url
          String redirectUrl = wxOpenService.buildQrConnectUrl(url,WxConsts.QrConnectScope.SNSAPI_LOGIN,URLEncoder.encode(returnUrl,"UTF-8"));

          log.info("【redirectUrl】 {}",redirectUrl);

          return "redirect:" + redirectUrl;
        }

        @GetMapping("/qrUserInfo")
        public String qrUserInfo(@RequestParam("code") String code,
                                 @RequestParam("state") String returnUrl){


                WxOAuth2AccessToken accessToken = new WxOAuth2AccessToken();
                try {
                        accessToken = wxOpenService.getOAuth2Service().getAccessToken(code);
                } catch (WxErrorException e) {
                        log.error("【微信网页授权】{}", e);
                        throw new SellException(ResultEnum.WECHAT_OPEN_ERROR.getCode(), e.getError().getErrorMsg());
                }
                log.info("wxMpOAuth2AccessToken={}", accessToken);
                String openId = accessToken.getOpenId();
                log.info("【openid】= {}",openId);
                log.info("【returnUrl】= {}",returnUrl);
                return "redirect:" + returnUrl + "?openid=" + openId;
        }




}
