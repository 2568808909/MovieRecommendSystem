package com.ccb.movie.service.impl;

import com.ccb.movie.bean.common.HttpResult;
import com.ccb.movie.service.AdminService;
import com.ccb.movie.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AdminServiceImpl implements AdminService {

    @Value("${movie.admin.name}")
    private String admin;

    @Value("${movie.admin.password}")
    private String password;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public HttpResult login(String admin, String password) {
        password = CommonUtil.MD5(password);
        if (this.password.equals(password) && this.admin.equals(admin)) {
            String token = CommonUtil.MD5(admin + System.currentTimeMillis());
            RedisScript<Void> script = new DefaultRedisScript<>("redis.call(\"SET\",KEYS[1],ARGV[1])\n" +
                    "redis.call(\"EXPIRE\",KEYS[1],tonumber(ARGV[2]))");  //使用lua脚本保证操作原子性
            redisTemplate.execute(script,
                    Collections.singletonList(token),
                    "admin",
                    "86400");
            return HttpResult.success("登陆成功", token);
        }
        return HttpResult.fail("登陆失败");
    }

    @Override
    public HttpResult logout(String token) {
        redisTemplate.delete(token);
        return HttpResult.success("登出成功");
    }

    @Override
    public boolean isLogin(String token) {
        return redisTemplate.boundValueOps(token).get() != null;
    }
}
