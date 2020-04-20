package com.ccb.springcloud.aspect;

import com.ccb.movie.bean.common.HttpResult;
import com.ccb.springcloud.controller.UserController;
import com.ccb.springcloud.feign.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Aspect
@Configuration
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
                    HttpResult login = userService.isLogin(token);
                    if (login.getData().equals(true)) {
                        return joinPoint.proceed();
                    }
                    break;
                }
            }
        }
        response.sendRedirect("/user/login");
        return HttpResult.fail(-2,"请先登录");
    }
}
