package com.imooc.controller;

import com.imooc.config.ProjectUrlConfig;
import com.imooc.constant.CookieConstant;
import com.imooc.constant.RedisConstant;
import com.imooc.dataonject.SellerInfo;
import com.imooc.enums.ResultEnum;
import com.imooc.service.SellerService;
import com.imooc.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/seller")
public class SellerUserController {

   @Autowired
    private SellerService sellerService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private  ProjectUrlConfig projectUrlConfig;

    /**
     * 用户登录
     * @param openid
     * @param response
     * @param map
     * @return
     */
    @GetMapping("/login")
    public ModelAndView login(@RequestParam("openid") String openid,
                      HttpServletResponse response,
                      Map<String,Object> map){

        //1.根据openid去查找对应的用户
        SellerInfo sellerInfo = sellerService.findSellerInfoByOpenid(openid);
        if(sellerInfo == null){
            map.put("msg", ResultEnum.LOGIN_FAIL.getMessage());
            map.put("url","/sell/seller/order/list");
            return new ModelAndView("common/error",map);
        }
        //2.将token写入redis
        String token = UUID.randomUUID().toString();
        Integer expire = RedisConstant.EXPIRE;// 过期时间
        //key = token_%s + token
        stringRedisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX,token),openid,expire,TimeUnit.SECONDS);

        //3.将token写入cookie
        CookieUtil.set(response, CookieConstant.TOKEN,token,CookieConstant.EXPIRE);

        return new ModelAndView("redirect:"+projectUrlConfig.getSell() +"/sell/seller/order/list");
    }

    /**
     * 用户登出
     */
    @GetMapping("/logout")
    public ModelAndView logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Map<String,Object> map){
        //1.从cookie里查询
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if(cookie != null){
            //2.清除redis
            stringRedisTemplate.opsForValue().getOperations().delete(String.format(RedisConstant.TOKEN_PREFIX,cookie.getValue()));
            //3.清除cookie--重新设置
            CookieUtil.set(response,CookieConstant.TOKEN,null,0);
        }
        map.put("msg",ResultEnum.LOGIN_SUCCESS.getMessage());
        map.put("url","/sell/seller/order/list");
        return new ModelAndView("common/success",map);


    }


}
