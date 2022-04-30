package com.youxiang.upload.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsFilterConfig {
    @Bean
    public CorsFilter corsFilter(){
        // 1.初始化配置对象
        CorsConfiguration configuration = new CorsConfiguration();
        // 允许跨域的域名，如果要携带cookie一定不能设置*
        configuration.addAllowedOrigin("http://manage.youxiang.com");
        configuration.addAllowedOrigin("http://www.youxiang.com");
        // 允许携带cookie
        configuration.setAllowCredentials(true);
        //允许所有请求方式跨域方法
        configuration.addAllowedMethod("*");
        // 允许携带头信息跨域访问
        configuration.addAllowedHeader("*");
        // 2.初始化配置源对象
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 拦截所有请求，效验是否允许跨域
        source.registerCorsConfiguration("/**",configuration);
        return new CorsFilter(source);
    }
}
