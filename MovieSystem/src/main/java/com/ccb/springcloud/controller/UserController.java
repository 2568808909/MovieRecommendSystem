package com.ccb.springcloud.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ccb.movie.bean.common.HttpResult;
import com.ccb.movie.bean.user.User;
import com.ccb.movie.bean.user.vo.*;
import com.ccb.movie.exception.BizException;
import com.ccb.springcloud.annotation.UserOps;
import com.ccb.springcloud.feign.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    public final static String TICKET = "ticket";

    @CrossOrigin(origins = "http://www.movie.com")
    @PostMapping("/register")
    public HttpResult register(@Validated @RequestBody UserRegisterParam param) {
        return userService.register(param);
    }

    @CrossOrigin(origins = "http://www.movie.com")
    @PostMapping("/login")
    public HttpResult login(@Validated @RequestBody UserLoginParam param,
                            HttpServletResponse response) {
        HttpResult loginInfo = userService.login(param);
        addLoginCookie(loginInfo, response);
        return loginInfo;
    }

    private void addLoginCookie(HttpResult result, HttpServletResponse response) {
        if (result.getCode() == HttpResult.SUCCESS_CODE) {
            Map data = (Map) result.getData();
            Cookie cookie = new Cookie(TICKET, (String) data.get("token"));
            cookie.setMaxAge(86400);  //要设置cookie有效时间才可生效
            cookie.setPath("/");
            cookie.setDomain("movie.com");
            response.addCookie(cookie);
        }
    }

    @GetMapping("/{id}")
    public HttpResult getUserById(@PathVariable("id") Long id) {
        if (id == null || id.compareTo(0L) <= 0) {
            throw new BizException("请输入正确的id");
        }
        return userService.getUserById(id);
    }

    @CrossOrigin(origins = "http://www.movie.com")
    @PutMapping("/psw")
    public HttpResult changePassword(@Validated @RequestBody UserChangePasswordParam param) {
        return userService.changePassword(param);
    }

    @CrossOrigin(origins = "http://www.movie.com")
    @PutMapping("/logout")
    @UserOps
    public HttpResult logout(@RequestBody @Validated LogoutParam param,
                             HttpServletResponse response,
                             HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (TICKET.equals(cookie.getName())) {
                    Cookie c = new Cookie(cookie.getName(), null);
                    c.setMaxAge(0);
                    c.setPath("/");
                    cookie.setDomain("movie.com");
                    response.addCookie(c);
                    break;
                }
            }
            return userService.logout(param);
        }
        return HttpResult.fail("没有您的登陆信息，无需登出");
    }

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("/user/login");
    }
}
