package com.ccb.movie.service;

import com.ccb.movie.bean.common.HttpPageResult;
import com.ccb.movie.bean.common.HttpResult;
import com.ccb.movie.bean.movie.Movie;
import com.ccb.movie.bean.movie.Rating;

public interface MovieService {

    void mark(Rating rating);

    HttpPageResult moviePage(Movie movie, Integer pageNum, Integer pageSize);

    int addMovie(Movie movie);

    int updateMovie(Movie movie);

    HttpResult offlineRecommend(Integer uid);

    HttpResult streamingRecommend(Integer uid);
}
