package com.ccb.movie.bean.user.vo;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ToString
public class UserRegisterParam {

    @NotNull(message = "请输入用户名")
    private String username;

    @NotNull(message = "请输入密码")
    private String password;

    @NotNull(message = "请输入手机号码")
    private String phone;

    @NotNull(message = "请输入电子邮箱")
    @Pattern(regexp = "[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+",message = "您输入的电子邮件格式不正确")
    private String email;

}
