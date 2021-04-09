package com.xiaozhuo.ctbsb.config;

import com.xiaozhuo.ctbsb.common.utils.EmailUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilsConfig {

    @Bean
    public EmailUtil emailUtil(){
        return new EmailUtil();
    }
}
