package com.ccb.movie.controller;

import com.ccb.movie.bean.common.HttpResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movie")
public class MovieController {

    @PutMapping("/mark")
    public HttpResult mark() {
        return null;
    }

    @GetMapping("/page")
    public HttpResult moviePage(){
        return null;
    }

    @PostMapping("/add")
    public HttpResult addMovie(){
        return null;
    }

    @PutMapping("/update")
    public HttpResult updateMovieInfo(){
        return null;
    }
}
