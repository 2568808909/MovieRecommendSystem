package com.ccb.movie.bean.user.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LogoutParam {

    @NotBlank(message = "token不能为空")
    private String token;

    @NotBlank(message = "请输入用户名")
    private String username;
}
