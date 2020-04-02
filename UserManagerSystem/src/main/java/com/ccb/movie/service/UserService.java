package com.ccb.movie.service;


import com.ccb.movie.bean.common.HttpResult;
import com.ccb.movie.bean.user.User;

public interface UserService {
    HttpResult register(User user);

    HttpResult login(String username, String password);

    HttpResult getUserById(Long id);
}
