package com.ccb.movie.test;

import com.ccb.movie.bean.user.User;
import com.ccb.movie.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

@SpringBootTest
public class UserManagmentSystemTest {

    @Autowired
    private UserService userService;

    @Test
    public void test() {
        Random random = new Random();
        User user = new User();
        Long phone = 15545964816L;
        for (int i = 1; i < 1000; i++) {
            user.setUsername("ccb" + i);
            user.setPassword("123456");
            user.setPhone(phone+i+10+"");
            user.setEmail("123@qq.com");
            user.setId(null);
            userService.register(user);
        }
    }
}
