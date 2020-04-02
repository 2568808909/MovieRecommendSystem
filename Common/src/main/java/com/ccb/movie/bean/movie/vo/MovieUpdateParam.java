package com.ccb.movie.bean.movie.vo;

import lombok.Data;
import org.apache.ibatis.annotations.Param;

import javax.validation.constraints.NotNull;

@Data
public class MovieUpdateParam {

    @NotNull(message = "请指定要更新的电影的id")
    private Long id;

    private String name;

    private String coverUrl;
}
