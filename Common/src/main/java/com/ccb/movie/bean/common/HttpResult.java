package com.ccb.movie.bean.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HttpResult implements Serializable {

    public transient final static int SUCCESS_CODE = 0;

    public transient final static String SUCCESS_MSG = "ok";

    public transient final static int DEFAULT_FAIL_CODE = -1;

    public transient final static String DEFAULT_FAIL_MSG = "fail";


    private int code;

    private String msg;

    private Object data;

    public static HttpResult success() {
        return success(null);
    }

    public static HttpResult success(Object data) {
        return new HttpResult(SUCCESS_CODE, SUCCESS_MSG, data);
    }

    public static HttpResult success(String msg, Object data) {
        return new HttpResult(SUCCESS_CODE, msg, data);
    }

    public static HttpResult fail(Throwable throwable) {
        return fail(throwable.getMessage());
    }

    public static HttpResult fail(String msg) {
        return fail(DEFAULT_FAIL_CODE, msg);
    }

    public static HttpResult fail(int code, String msg) {
        return new HttpResult(code, msg, null);
    }
}
