package com.ccb.movie.feign;

import com.ccb.movie.bean.common.HttpResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("MOVIE-USER-SERVICE")
public interface UserService {

    @GetMapping("/user/{id}")
    HttpResult getUserById(@PathVariable("id") Long id);
}
