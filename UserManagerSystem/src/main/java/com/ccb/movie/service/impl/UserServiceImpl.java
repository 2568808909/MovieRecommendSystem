package com.ccb.movie.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ccb.movie.bean.common.HttpResult;
import com.ccb.movie.bean.user.User;
import com.ccb.movie.bean.user.vo.LoginMessage;
import com.ccb.movie.bean.user.vo.UserChangePasswordParam;
import com.ccb.movie.exception.BizException;
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
        user.setPassword(CommonUtil.MD5(user.getPassword()));
        Date now = new Date();
        user.setCreatedTime(now);
        user.setUpdatedTime(now);
        try {
            int res = userMapper.insert(user);
            return HttpResult.success(res == 1 ? "注册成功" : "注册失败");
        } catch (DuplicateKeyException e) {
            return HttpResult.fail("用户名已存在");
        }
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

    @Override
    public HttpResult changePassword(UserChangePasswordParam param) {
        User select = new User();
        select.setUsername(param.getUsername());
        select.setPassword(CommonUtil.MD5(param.getOriginPassword()));
        User user = userMapper.selectOne(select);
        if (user == null) {
            throw new BizException("用户名或原密码不正确");
        }
        if (!param.getNewPassword().equals(param.getConfirm())) {
            throw new BizException("两次输入的新密码不一致");
        }
        User update = new User();
        update.setUpdatedTime(new Date());
        update.setId(user.getId());
        update.setPassword(CommonUtil.MD5(param.getNewPassword()));
        return userMapper.updateByPrimaryKeySelective(update) == 1 ?
                HttpResult.success() : HttpResult.fail("更新失败了，请稍后再试");
    }

    @Override
    public HttpResult logout(String token, String username) {
        String user = (String) Optional.ofNullable(redisTemplate.boundValueOps(token).get())
                .map(JSONObject::parseObject)
                .map(obj -> obj.get("username"))
                .orElse(null);
        if (user == null || username.equals(user)) {
            redisTemplate.delete(token+"==");
            return HttpResult.success("登出成功");
        }
        return HttpResult.fail("请确认token或者username的正确性，token与用户名不匹配");
    }

    @Override
    public Boolean isLogin(String token) {
        return redisTemplate.boundValueOps(token).get() != null;
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
            LoginMessage loginMessage=new LoginMessage();
            loginMessage.setToken(token);
            loginMessage.setUser(select.get(0));
            return HttpResult.success(loginMessage);
        } else {
            return HttpResult.fail("用户名或密码错误");
        }
    }
}
