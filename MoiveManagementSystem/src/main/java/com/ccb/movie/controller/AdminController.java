package com.ccb.movie.controller;

import com.ccb.movie.bean.common.HttpResult;
import com.ccb.movie.bean.user.vo.UserLoginParam;
import com.ccb.movie.exception.BizException;
import com.ccb.movie.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    public final static String TICKET = "adminTicket";

    @CrossOrigin("www.movie.com")
    @PostMapping("/login")
    public HttpResult login(@Validated @RequestBody UserLoginParam param, HttpServletResponse response) {
        String admin = param.getUsername();
        String password = param.getPassword();
        if (StringUtils.isEmpty(admin) || StringUtils.isEmpty(password)) {
            throw new BizException("请输入管理员账号和密码");
        }
        HttpResult loginInfo = adminService.login(admin, password);
        if (loginInfo.getCode() == HttpResult.SUCCESS_CODE) {
            Cookie cookie = new Cookie(TICKET, (String) loginInfo.getData());
            cookie.setPath("/");
            cookie.setMaxAge(86400);
            cookie.setDomain("movie.com");
            response.addCookie(cookie);
        }
        return loginInfo;
    }

    @CrossOrigin("www.movie.com")
    @PutMapping("/logout")
    public HttpResult logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (TICKET.equals(cookie.getName())) {
                    Cookie c = new Cookie(cookie.getName(), null);
                    c.setMaxAge(0);
                    c.setPath("/");
                    c.setDomain("movie.com");
                    response.addCookie(c);
                    return adminService.logout(cookie.getValue());
                }
            }
        }
        return HttpResult.success();
    }

    @GetMapping("/login")
    public ModelAndView loginPage() {
        return new ModelAndView("/admin/login");
    }
}
