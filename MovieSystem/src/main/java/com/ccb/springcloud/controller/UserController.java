package com.ccb.springcloud.controller;

import com.ccb.movie.bean.common.HttpResult;
import com.ccb.movie.bean.user.vo.UserLoginParam;
import com.ccb.movie.bean.user.vo.UserRegisterParam;
import com.ccb.movie.exception.BizException;
import com.ccb.springcloud.feign.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    HttpResult register(@Validated @RequestBody UserRegisterParam param) {
        return userService.register(param);
    }

    @PostMapping("/login")
    HttpResult login(@Validated @RequestBody UserLoginParam param) {
        return userService.login(param);
    }

    @GetMapping("/{id}")
    HttpResult getUserById(@PathVariable("id") Long id) {
        if (id == null || id.compareTo(0L) <= 0) {
            throw new BizException("请输入正确的id");
        }
        return userService.getUserById(id);
    }
}
