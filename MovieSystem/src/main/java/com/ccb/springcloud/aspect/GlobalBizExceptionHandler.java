package com.ccb.springcloud.aspect;

import com.ccb.movie.bean.common.HttpResult;
import com.ccb.movie.exception.BizException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalBizExceptionHandler {
    @ExceptionHandler(BizException.class)
    @ResponseBody
    public HttpResult handler(HttpServletRequest request,
                              HttpServletResponse response,
                              Exception ex) {
        return HttpResult.fail(ex);
    }
}
