package com.ccb.springcloud.controller;

import com.ccb.movie.bean.common.HttpResult;
import com.ccb.movie.bean.movie.vo.MovieAddParam;
import com.ccb.movie.bean.movie.vo.MovieSearchParam;
import com.ccb.movie.bean.movie.vo.MovieUpdateParam;
import com.ccb.movie.bean.movie.vo.RatingParam;
import com.ccb.springcloud.feign.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping("/mark")
    public HttpResult mark(RatingParam param) {
        return movieService.mark(param);
    }

    @GetMapping("/page")
    public HttpResult moviePage(@Validated MovieSearchParam param) {
        return movieService.moviePage(param);
    }

}
