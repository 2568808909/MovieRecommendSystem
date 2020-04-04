package com.ccb.springcloud.controller;

import com.ccb.movie.bean.common.HttpResult;
import com.ccb.movie.bean.user.vo.LogoutParam;
import com.ccb.movie.bean.user.vo.UserChangePasswordParam;
import com.ccb.movie.bean.user.vo.UserLoginParam;
import com.ccb.movie.bean.user.vo.UserRegisterParam;
import com.ccb.movie.exception.BizException;
import com.ccb.springcloud.feign.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public HttpResult register(@Validated @RequestBody UserRegisterParam param) {
        return userService.register(param);
    }

    @PostMapping("/login")
    public HttpResult login(@Validated @RequestBody UserLoginParam param, HttpServletResponse response) {
        HttpResult loginInfo = userService.login(param);
        if (loginInfo.getCode() == HttpResult.SUCCESS_CODE) {
            Cookie cookie = new Cookie("ticket", (String) loginInfo.getData());
            response.addCookie(cookie);
        }
        return loginInfo;
    }

    @GetMapping("/{id}")
    public HttpResult getUserById(@PathVariable("id") Long id) {
        if (id == null || id.compareTo(0L) <= 0) {
            throw new BizException("请输入正确的id");
        }
        return userService.getUserById(id);
    }

    @PutMapping("/psw")
    public HttpResult changePassword(@Validated @RequestBody UserChangePasswordParam param) {
        return userService.changePassword(param);
    }

    @PutMapping("/logout")
    HttpResult logout(@Validated @RequestBody LogoutParam param) {
        return userService.logout(param);
    }
}
