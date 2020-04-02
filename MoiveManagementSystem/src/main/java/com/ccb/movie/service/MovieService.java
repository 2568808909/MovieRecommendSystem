package com.ccb.movie.service;

import com.ccb.movie.bean.common.PageInfo;
import com.ccb.movie.bean.movie.Movie;
import com.ccb.movie.bean.movie.Rating;

public interface MovieService {

    void mark(Rating rating);

    PageInfo moviePage();

    int addMovie(Movie movie);

    int updateMovie(Movie movie);
}
