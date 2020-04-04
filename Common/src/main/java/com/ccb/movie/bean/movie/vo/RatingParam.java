package com.ccb.movie.bean.movie.vo;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class RatingParam {

    @NotNull(message = "请输入电影id")
    private Long movieId;

    @NotNull(message = "请输入用户id")
    private Long userId;

    @NotNull(message = "请输入评分")
    @Min(value = 1,message = "评分最小值为1")
    @Max(value = 5,message = "评分最大值为5")
    private Integer rating;
}
