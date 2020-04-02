package com.ccb.movie.mapper;

import com.ccb.movie.bean.movie.Movie;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface MovieMapper extends Mapper<Movie> {

    List<Movie> moviePage(@Param("movie") Movie movie,
                          @Param("pageStart") Integer pageStart,
                          @Param("pageSize") Integer pageSize);

    long count(@Param("movie") Movie movie);
}
