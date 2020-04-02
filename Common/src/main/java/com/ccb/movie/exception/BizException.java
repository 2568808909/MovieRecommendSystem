package com.ccb.movie.exception;

public class BizException extends RuntimeException {

    public BizException(String msg) {
        super(msg);
    }

    public BizException(Throwable throwable) {
        super(throwable);
    }
}
