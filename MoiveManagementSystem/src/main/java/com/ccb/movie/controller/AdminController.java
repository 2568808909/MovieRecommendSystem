package com.ccb.movie.controller;

import com.ccb.movie.bean.common.HttpResult;
import com.ccb.movie.exception.BizException;
import com.ccb.movie.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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

    @PostMapping("/login")
    public HttpResult login(String admin, String password, HttpServletResponse response) {
        if (StringUtils.isEmpty(admin) || StringUtils.isEmpty(password)) {
            throw new BizException("请输入管理员账号和密码");
        }
        HttpResult loginInfo = adminService.login(admin, password);
        if (loginInfo.getCode() == HttpResult.SUCCESS_CODE) {
            Cookie cookie = new Cookie(TICKET, (String) loginInfo.getData());
            cookie.setPath("/");
            cookie.setMaxAge(86400);
            response.addCookie(cookie);
        }
        return loginInfo;
    }

    @PutMapping("/logout")
    public HttpResult logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (TICKET.equals(cookie.getName())) {
                    Cookie c = new Cookie(cookie.getName(), null);
                    c.setMaxAge(0);
                    c.setPath("/");
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
