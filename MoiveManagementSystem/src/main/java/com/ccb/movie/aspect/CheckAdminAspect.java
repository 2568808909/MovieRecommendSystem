package com.ccb.movie.aspect;

import com.ccb.movie.bean.common.HttpResult;
import com.ccb.movie.controller.AdminController;
import com.ccb.movie.service.AdminService;
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
public class CheckAdminAspect {

    @Autowired
    private AdminService adminService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Around("@annotation(com.ccb.movie.annation.AdminOps)")
    public Object checkAdmin(ProceedingJoinPoint joinPoint) throws Throwable {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (AdminController.TICKET.equals(cookie.getName())) {
                    String token = cookie.getValue();
                    if (adminService.isLogin(token)) {
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
