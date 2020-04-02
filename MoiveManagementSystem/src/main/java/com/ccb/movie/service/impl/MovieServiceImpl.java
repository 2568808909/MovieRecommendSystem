package com.ccb.movie.service.impl;

import com.ccb.movie.bean.common.PageInfo;
import com.ccb.movie.bean.movie.Movie;
import com.ccb.movie.bean.movie.Rating;
import com.ccb.movie.exception.BizException;
import com.ccb.movie.mapper.MovieMapper;
import com.ccb.movie.mapper.RatingMapper;
import com.ccb.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private RatingMapper ratingMapper;

    @Override
    public void mark(Rating rating) {
        try {
            ratingMapper.insert(rating);
        }catch (DuplicateKeyException e){
            throw new BizException("您已经对该电影评过分了哦，不能多次评分哦！");
        }
    }

    @Override
    public PageInfo moviePage() {
        return null;
    }

    @Override
    public int addMovie(Movie movie) {
        return 0;
    }

    @Override
    public int updateMovie(Movie movie) {
        return 0;
    }
}
