package com.ccb.movie.aspect;

import com.ccb.movie.bean.common.HttpResult;
import com.ccb.movie.exception.BizException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class UserBizExceptionHandler {

    @ExceptionHandler(BizException.class)
    @ResponseBody
    public HttpResult handler(HttpServletRequest request,
                              HttpServletResponse response,
                              Exception ex) {
        return HttpResult.fail(ex);
    }
}
