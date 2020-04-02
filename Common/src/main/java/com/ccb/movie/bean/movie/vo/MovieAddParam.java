package com.ccb.movie.bean.movie.vo;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
public class MovieAddParam {
    @NotNull
    private String name;

    @NotNull
    private String coverUrl;
}
