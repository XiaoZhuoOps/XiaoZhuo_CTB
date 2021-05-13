package com.xiaozhuo.ctbsb.security.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.xiaozhuo.ctbsb.common.exception.Asserts;
import com.xiaozhuo.ctbsb.modules.user.model.User;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Date;

/**
 * JWT 工具类
 */
public class JwtTokenUtil{

    @Value("${jwt.SECRET}")
    private String SECRET;

    @Value("${jwt.EXPIRATION_TIME}")
    private long EXPIRATION_TIME;

    public String getToken(User user) {
        String token="";
        token= JWT.create()
                .withClaim("userId",user.getId())
                .withSubject(user.getName())
                .withExpiresAt(new Date( Instant.now().toEpochMilli() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET));
        return token;
    }

    public int getUserId(String token) {
        int userId = 0;
        try {
            userId = JWT.decode(token).getClaims().get("userId").asInt();
        } catch (Exception e){
            Asserts.fail("token解析错误");
        }
        return userId;
    }
}