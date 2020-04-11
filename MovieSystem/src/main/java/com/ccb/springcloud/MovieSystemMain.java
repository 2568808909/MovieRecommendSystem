package com.ccb.springcloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableFeignClients
@EnableAspectJAutoProxy
@MapperScan(basePackages = "com.ccb.springcloud.mapper")
public class MovieSystemMain {
    public static void main(String[] args) {
        SpringApplication.run(MovieSystemMain.class, args);
    }
}
