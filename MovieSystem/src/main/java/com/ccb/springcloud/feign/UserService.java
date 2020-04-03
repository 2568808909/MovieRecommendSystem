package com.ccb.springcloud.feign;

import com.ccb.movie.bean.common.HttpResult;
import com.ccb.movie.bean.user.User;
import com.ccb.movie.bean.user.vo.UserLoginParam;
import com.ccb.movie.bean.user.vo.UserRegisterParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("MOVIE-USER-SERVICE")
public interface UserService {

    @PostMapping("/register")
    HttpResult register(UserRegisterParam param);

    @PostMapping("/login")
    HttpResult login(UserLoginParam param);

    @GetMapping("/{id}")
    HttpResult getUserById(@PathVariable("id") Long id);
}