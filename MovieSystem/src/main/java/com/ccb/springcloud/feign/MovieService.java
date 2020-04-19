package com.ccb.springcloud.feign;

import com.ccb.movie.bean.common.HttpPageResult;
import com.ccb.movie.bean.common.HttpResult;
import com.ccb.movie.bean.movie.vo.MovieSearchParam;
import com.ccb.movie.bean.movie.vo.RatingParam;
import com.ccb.movie.bean.movie.vo.Recommend;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("MOVIE-MANAGEMENT-SERVICE")
public interface MovieService {

    @PostMapping("/movie/mark")
    HttpResult mark(RatingParam param);

    @GetMapping("/movie/page")
    HttpPageResult moviePage(MovieSearchParam param);

    @GetMapping("/movie/offline/recommend/{uid}")
    HttpResult offlineRecommend(@PathVariable("uid") Integer uid);

    @GetMapping("/movie/streaming/recommend/{uid}")
    HttpResult streamingRecommend(@PathVariable("uid") Integer uid);
}