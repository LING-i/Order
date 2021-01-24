package com.imooc.utils;

import com.imooc.VO.ResultVO;

public class ResultVOUtil {


    //成功的方法
    public static ResultVO success(Object object){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        resultVO.setData(object);
        return  resultVO;
    }

    //成功但无数据返回的方法
    public static ResultVO success(){
        return  success(null);
    }

    //错误方法
    public static ResultVO error(Integer code,String msg){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        return resultVO;
    }

}
