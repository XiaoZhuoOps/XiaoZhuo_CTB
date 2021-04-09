package com.xiaozhuo.ctbsb.config;

import com.xiaozhuo.ctbsb.security.util.JwtTokenUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {
    @Bean
    public JwtTokenUtil getJwtTokenUtil(){
        return new JwtTokenUtil();
    }
}
