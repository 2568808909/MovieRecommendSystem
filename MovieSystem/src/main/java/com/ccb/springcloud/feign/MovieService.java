package com.ccb.springcloud.feign;

import com.ccb.movie.bean.common.HttpResult;
import com.ccb.movie.bean.movie.vo.MovieSearchParam;
import com.ccb.movie.bean.movie.vo.RatingParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("MOVIE-MANAGEMENT-SERVICE")
public interface MovieService {

    @PostMapping("/movie/mark")
    HttpResult mark(RatingParam param);

    @GetMapping("/movie/page")
    HttpResult moviePage(MovieSearchParam param);

}