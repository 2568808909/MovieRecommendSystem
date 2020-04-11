package com.ccb.springcloud.mapper;

import com.ccb.movie.bean.movie.Movie;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MovieMapper{

    List<Movie> selectByMovieIds(@Param("movies") List<Integer> movies);

}
