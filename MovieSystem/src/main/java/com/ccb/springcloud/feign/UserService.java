package com.ccb.springcloud.feign;

import com.ccb.movie.bean.common.HttpResult;
import com.ccb.movie.bean.user.vo.LogoutParam;
import com.ccb.movie.bean.user.vo.UserChangePasswordParam;
import com.ccb.movie.bean.user.vo.UserLoginParam;
import com.ccb.movie.bean.user.vo.UserRegisterParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@FeignClient("MOVIE-USER-SERVICE")
public interface UserService {

    @PostMapping("/register")
    HttpResult register(UserRegisterParam param);

    @PostMapping("/login")
    HttpResult login(UserLoginParam param);

    @GetMapping("/{id}")
    HttpResult getUserById(@PathVariable("id") Long id);

    @PutMapping("/psw")
    HttpResult changePassword(@Validated @RequestBody UserChangePasswordParam param);

    @PutMapping("/logout")
    HttpResult logout(@Validated @RequestBody LogoutParam param);

    @GetMapping("/isLogin")
    boolean isLogin(String token);
}