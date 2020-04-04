package com.ccb.movie.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class CheckAdminAspect {

    @Around("@annotation(com.ccb.movie.annation.AdminOps)")
    public void checkAdmin(ProceedingJoinPoint joinPoint)throws Throwable {

    }
}
