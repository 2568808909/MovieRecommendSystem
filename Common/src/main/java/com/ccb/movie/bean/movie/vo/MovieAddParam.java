package com.ccb.movie.bean.movie.vo;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
public class MovieAddParam {

    @NotNull(message = "请输入电影标题")
    private String name;

    @NotNull(message = "请输入电影封面url")
    private String coverUrl;
}
