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

    @PostMapping("/user/register")
    HttpResult register(@RequestBody UserRegisterParam param);

    @PostMapping("/user/login")
    HttpResult login(@RequestBody UserLoginParam param);

    @GetMapping("/user/{id}")
    HttpResult getUserById(@PathVariable("id") Long id);

    @PutMapping("/user/psw")
    HttpResult changePassword(@Validated @RequestBody UserChangePasswordParam param);

    @PutMapping("/user/logout")
    HttpResult logout(@Validated @RequestBody LogoutParam param);

    @GetMapping("/user/isLogin")
    HttpResult isLogin(@RequestBody String token);
}