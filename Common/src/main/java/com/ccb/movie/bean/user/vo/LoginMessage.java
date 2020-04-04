package com.ccb.movie.bean.user.vo;

import com.ccb.movie.bean.user.User;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class LoginMessage implements Serializable {

    private String token;

    private User user;
}
