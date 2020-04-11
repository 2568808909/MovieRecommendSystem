package com.ccb.springcloud.service;

import com.ccb.movie.bean.common.HttpResult;
import com.ccb.movie.bean.movie.vo.MovieSearchParam;
import com.ccb.movie.bean.movie.vo.RatingParam;
import org.springframework.web.bind.annotation.GetMapping;

public interface MovieWebService {

    HttpResult offlineRecommend(Integer uid);

    HttpResult onlineRecommend(Integer uid);

    HttpResult mark(RatingParam param);

    HttpResult moviePage(MovieSearchParam param);
}
