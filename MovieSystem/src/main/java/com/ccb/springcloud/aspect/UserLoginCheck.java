package com.ccb.springcloud.aspect;

import com.ccb.movie.bean.common.HttpResult;
import com.ccb.springcloud.controller.UserController;
import com.ccb.springcloud.feign.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Aspect
public class UserLoginCheck {
    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Around("@annotation(com.ccb.springcloud.annotation.UserOps)")
    public Object checkAdmin(ProceedingJoinPoint joinPoint) throws Throwable {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (UserController.TICKET.equals(cookie.getName())) {
                    String token = cookie.getValue();
                    if (userService.isLogin(token)) {
                        return joinPoint.proceed();
                    }
                    break;
                }
            }
        }
        response.sendRedirect("/admin/login");
        return HttpResult.fail("请先登录");
    }
}
