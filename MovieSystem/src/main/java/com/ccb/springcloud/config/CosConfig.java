package com.ccb.springcloud.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class CosConfig implements WebMvcConfigurer {

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://www.movie.com")
//                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowCredentials(true)
//                .allowedHeaders("content-type",
//                        "Content-Type",
//                        "X-Requested-With",
//                        "accept",
//                        "Origin",
//                        "Access-Control-Request-Method",
//                        "Cookie", "Set-Cookie", "Authorization")
//                .maxAge(3600);
//    }
}
