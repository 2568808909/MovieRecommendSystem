package com.ccb.movie.bean.movie.vo;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class MovieSearchParam {
    private Long id;

    private String name;

    private String coverUrl;

    @NotNull(message = "pageNum不能为空")
    @Min(value = 1,message = "页码不能小于1")
    private Integer pageNum;

    @NotNull(message = "pageSize不能为空")
    @Min(value = 5,message = "pageSize最小取值为5")
    @Max(value = 100,message = "pageSize最大取值为100")
    private Integer pageSize;
}
