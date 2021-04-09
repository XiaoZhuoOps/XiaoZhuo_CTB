package com.xiaozhuo.ctbsb.config;


import com.xiaozhuo.ctbsb.common.config.BaseSwaggerConfig;
import com.xiaozhuo.ctbsb.common.domain.SwaggerProperties;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.xiaozhuo.ctbsb.modules")
                .title("CTBSB")
                .description("CTBSB相关接口文档")
                .contactName("hjq")
                .version("1.0")
                .enableSecurity(true)
                .build();
    }
}
