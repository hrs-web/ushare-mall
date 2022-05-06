package com.youxiang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.youxiang.user.mapper")
public class YouxiangUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(YouxiangUserApplication.class,args);
    }
}
