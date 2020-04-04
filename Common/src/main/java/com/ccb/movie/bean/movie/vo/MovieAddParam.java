package com.ccb.movie.bean.movie.vo;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@ToString
public class MovieAddParam {

    @NotBlank(message = "请输入电影标题")
    private String name;

    @NotBlank(message = "请输入电影封面url")
    private String coverUrl;
}
