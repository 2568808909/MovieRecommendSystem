package com.ccb.springcloud.controller;

import com.ccb.movie.bean.common.HttpPageResult;
import com.ccb.movie.bean.common.HttpResult;
import com.ccb.movie.bean.movie.vo.MovieSearchParam;
import com.ccb.movie.bean.movie.vo.RatingParam;
import com.ccb.movie.exception.BizException;
import com.ccb.springcloud.annotation.UserOps;
import com.ccb.springcloud.service.MovieWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private MovieWebService movieWebService;

    @CrossOrigin("www.movie.com")
    @PostMapping("/mark")
    @UserOps
    public HttpResult mark(@RequestBody @Validated RatingParam param) {
        return movieWebService.mark(param);
    }

    @GetMapping("/page")
    public HttpResult moviePage(@Validated MovieSearchParam param) {
        return movieWebService.moviePage(param);
    }

    @GetMapping("/offline/recommend/{uid}")
    @UserOps
    public HttpResult offlineRecommend(@PathVariable("uid") Integer uid) {
        if (uid <= 0) throw new BizException("请输入正确的uid");
        return movieWebService.offlineRecommend(uid);
    }

    @GetMapping("/streaming/recommend/{uid}")
    @UserOps
    public HttpResult streamingRecommend(@PathVariable("uid") Integer uid) {
        if (uid <= 0) throw new BizException("请输入正确的uid");
        return movieWebService.onlineRecommend(uid);
    }
}
