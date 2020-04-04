package com.ccb.movie.service;

import com.ccb.movie.bean.common.HttpResult;

public interface AdminService {

    HttpResult login(String admin, String password);

    HttpResult logout(String token);

    boolean isLogin(String token);
}