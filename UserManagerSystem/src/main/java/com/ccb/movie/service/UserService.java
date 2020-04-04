package com.ccb.movie.service;


import com.ccb.movie.bean.common.HttpResult;
import com.ccb.movie.bean.user.User;
import com.ccb.movie.bean.user.vo.UserChangePasswordParam;

public interface UserService {
    HttpResult register(User user);

    HttpResult login(String username, String password);

    HttpResult getUserById(Long id);

    HttpResult changePassword(UserChangePasswordParam param);

    HttpResult logout(String token, String username);

    boolean isLogin(String token);
}
