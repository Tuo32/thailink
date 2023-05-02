package com.liu.thailink.utils;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;

//this is used to generate token
public class TokenUtils {

    //generate token
    public static String genToken(String userID, String sign){
        return  JWT.create().withAudience(userID).withExpiresAt(DateUtil.offsetHour(new Date(), 2)).sign(Algorithm.HMAC256(sign));
    }


}
