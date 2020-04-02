package com.ccb.movie.controller;

import com.ccb.movie.bean.common.HttpResult;
import com.ccb.movie.bean.user.User;
import com.ccb.movie.exception.BizException;
import com.ccb.movie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public HttpResult register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public HttpResult login(@RequestBody User user) {
        return userService.login(user.getUsername(), user.getPassword());
    }

    @GetMapping("/{id}")
    public HttpResult getUserById(@PathVariable("id") Long id){
        if(id==null){
            throw new BizException("id不能为空");
        }
        return userService.getUserById(id);
    }
}
