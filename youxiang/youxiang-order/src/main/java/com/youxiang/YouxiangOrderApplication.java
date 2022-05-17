package com.youxiang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.FeignClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@FeignClient
@MapperScan("com.youxiang.order.mapper")
public class YouxiangOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(YouxiangOrderApplication.class,args);
    }
}
