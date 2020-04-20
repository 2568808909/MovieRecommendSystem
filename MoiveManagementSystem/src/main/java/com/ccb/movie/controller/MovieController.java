package com.ccb.movie.controller;

import com.ccb.movie.annation.AdminOps;
import com.ccb.movie.bean.common.HttpPageResult;
import com.ccb.movie.bean.common.HttpResult;
import com.ccb.movie.bean.movie.Movie;
import com.ccb.movie.bean.movie.Rating;
import com.ccb.movie.bean.movie.vo.MovieAddParam;
import com.ccb.movie.bean.movie.vo.MovieSearchParam;
import com.ccb.movie.bean.movie.vo.MovieUpdateParam;
import com.ccb.movie.bean.movie.vo.RatingParam;
import com.ccb.movie.exception.BizException;
import com.ccb.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@RestController
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping("/mark")
    public HttpResult mark(@Validated @RequestBody RatingParam param) {
        Rating rating = new Rating();
        rating.setUserId(param.getUserId());
        rating.setMovieId(param.getMovieId());
        rating.setRating(param.getRating());
        movieService.mark(rating);
        return HttpResult.success();
    }

    @CrossOrigin("www.movie.com")
    @GetMapping("/list")
    public HttpPageResult list(@Validated MovieSearchParam param) {
        Movie movie = new Movie();
        movie.setName(param.getName());
        movie.setCoverUrl(param.getCoverUrl());
        movie.setId(param.getId());
        return movieService.moviePage(movie, param.getPageNum(), param.getPageSize());
    }

    @CrossOrigin("www.movie.com")
    @GetMapping("/page")
    public HttpPageResult moviePage(@Validated @RequestBody MovieSearchParam param) {
        Movie movie = new Movie();
        movie.setName(param.getName());
        movie.setCoverUrl(param.getCoverUrl());
        movie.setId(param.getId());
        return movieService.moviePage(movie, param.getPageNum(), param.getPageSize());
    }

    @CrossOrigin("www.movie.com")
    @PostMapping("/edit")
    @AdminOps
    public HttpResult addMovie(@RequestBody MovieAddParam param) {
        Movie movie = new Movie();
        movie.setCoverUrl(param.getCoverUrl());
        movie.setName(param.getName());
        movieService.addMovie(movie);
        return HttpResult.success();
    }

    @CrossOrigin("www.movie.com")
    @PutMapping("/edit")
    @AdminOps
    public HttpResult updateMovieInfo(@Validated @RequestBody MovieUpdateParam param) {
        String name = param.getName();
        String coverUrl = param.getCoverUrl();
        if (StringUtils.isEmpty(name) && StringUtils.isEmpty(coverUrl)) {
            HttpResult.fail("传入数据都为空，没有需要更新的必要");
        }
        Movie movie = new Movie();
        movie.setName("".equals(name) ? null : name);
        movie.setCoverUrl("".equals(coverUrl) ? null : coverUrl);
        movie.setId(param.getId());
        movieService.updateMovie(movie);
        return HttpResult.success();
    }

    @GetMapping("/offline/recommend/{uid}")
    public HttpResult offlineRecommend(@PathVariable("uid") Integer uid) {
        if (uid < 0) throw new BizException("请输入正确的用户id");
        return movieService.offlineRecommend(uid);
    }

    @GetMapping("/streaming/recommend/{uid}")
    public HttpResult streamingRecommend(@PathVariable("uid") Integer uid) {
        if (uid < 0) throw new BizException("请输入正确的用户id");
        return movieService.streamingRecommend(uid);
    }

    @GetMapping("/index")
    public ModelAndView index() {
        return new ModelAndView("/admin/index");
    }
}
