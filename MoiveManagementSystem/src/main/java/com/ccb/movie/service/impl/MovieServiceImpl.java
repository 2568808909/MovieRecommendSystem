package com.ccb.movie.service.impl;

import com.ccb.movie.bean.common.HttpPageResult;
import com.ccb.movie.bean.common.HttpResult;
import com.ccb.movie.bean.common.PageInfo;
import com.ccb.movie.bean.movie.Movie;
import com.ccb.movie.bean.movie.Rating;
import com.ccb.movie.exception.BizException;
import com.ccb.movie.feign.UserService;
import com.ccb.movie.mapper.MovieMapper;
import com.ccb.movie.mapper.RatingMapper;
import com.ccb.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private RatingMapper ratingMapper;

    @Autowired
    private UserService userService;

    @Transactional
    @Override
    public void mark(Rating rating) {
        try {
            if (movieMapper.selectByPrimaryKey(rating.getMovieId()) == null) {
                throw new BizException("您所评价的电影不存在: " + rating.getMovieId());
            }
            if (userService.getUserById(rating.getUserId()).getData() == null) {
                throw new BizException("用户不存在: " + rating.getUserId());
            }
            Date now = new Date();
            rating.setCreatedTime(now);
            rating.setUpdatedTime(now);
            ratingMapper.insert(rating);
        } catch (DuplicateKeyException e) {
            throw new BizException("您已经对该电影评过分了哦，不能多次评分哦！");
        }
    }

    @Override
    public HttpResult moviePage(Movie movie, Integer pageNum, Integer pageSize) {
        long count = movieMapper.count(movie);
        Integer pageStart = (pageNum - 1) * pageSize;
        List<Movie> movies = movieMapper.moviePage(movie, pageStart, pageSize);
        PageInfo pageInfo = new PageInfo();
        pageInfo.setLastPage(pageStart + pageSize >= count);
        pageInfo.setTotalCount(count);
        pageInfo.setPageSize(pageSize);
        return HttpPageResult.success(movies, pageInfo);
    }

    @Override
    public int addMovie(Movie movie) {
        try {
            Date now = new Date();
            movie.setCreatedTime(now);
            movie.setUpdatedTime(now);
//            System.out.println("-------------------"+movie);
            return movieMapper.insert(movie);
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            throw new BizException("插入失败,键值重复");
        }
    }

    @Override
    public int updateMovie(Movie movie) {
        movie.setUpdatedTime(new Date());
        return movieMapper.updateByPrimaryKeySelective(movie);
    }
}
