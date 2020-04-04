package com.ccb.movie.bean.user.vo;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@ToString
public class UserChangePasswordParam {

    @NotBlank(message = "请输入用户名")
    private String username;

    @NotBlank(message = "请输入原密码")
    private String originPassword;

    @NotBlank(message = "请输入新密码")
    private String newPassword;

    @NotBlank(message = "请再次确认新密码")
    private String confirm;

}
