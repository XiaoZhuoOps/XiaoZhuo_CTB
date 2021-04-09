package com.xiaozhuo.ctbsb.config;


import com.xiaozhuo.ctbsb.common.config.BaseRedisConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class RedisConfig extends BaseRedisConfig {

}
