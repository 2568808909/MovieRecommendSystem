package com.ccb.movie.service.impl;

import com.alibaba.fastjson.JSON;
import com.ccb.movie.bean.common.HttpResult;
import com.ccb.movie.bean.user.User;
import com.ccb.movie.mapper.UserMapper;
import com.ccb.movie.service.UserService;
import com.ccb.movie.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public HttpResult register(User user) {
        System.out.println(user);
        user.setPassword(CommonUtil.MD5(user.getPassword()));
        Date now = new Date();
        user.setCreatedTime(now);
        user.setUpdatedTime(now);
        try {
            userMapper.insert(user);
            loginWithMD5(user.getUsername(), user.getPassword());
        } catch (DuplicateKeyException e) {
            return HttpResult.fail("用户名已存在");
        }
        return HttpResult.success("注册成功", user);
    }

    @Override
    public HttpResult login(String username, String password) {
        password = CommonUtil.MD5(password);
        return loginWithMD5(username, password);
    }

    @Override
    public HttpResult getUserById(Long id) {
        return Optional.ofNullable(userMapper.selectByPrimaryKey(id))
                .map(HttpResult::success)
                .orElse(HttpResult.fail("用户不存在"));
    }

    private HttpResult loginWithMD5(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        List<User> select = userMapper.select(user);
        if (select.size() != 0) {
            String token = CommonUtil.MD5(username + System.currentTimeMillis());
            String userStr = JSON.toJSONString(user);
            redisTemplate.opsForValue().set(token, userStr);
            return HttpResult.success(token);
        } else {
            return HttpResult.fail("用户名或密码错误");
        }
    }
}
