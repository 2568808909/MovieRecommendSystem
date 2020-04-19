package com.ccb.springcloud.service.impl;

import com.ccb.movie.bean.common.HttpPageResult;
import com.ccb.movie.bean.common.HttpResult;
import com.ccb.movie.bean.movie.Movie;
import com.ccb.movie.bean.movie.vo.MovieSearchParam;
import com.ccb.movie.bean.movie.vo.RatingParam;
import com.ccb.movie.bean.movie.vo.Recommend;
import com.ccb.springcloud.feign.MovieService;
import com.ccb.springcloud.mapper.MovieMapper;
import com.ccb.springcloud.service.MovieWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class MovieWebServiceImpl implements MovieWebService {

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieMapper movieMapper;

    @Value("${image.path}")
    private String imagePath;

    @Override
    public HttpResult offlineRecommend(Integer uid) {
        HttpResult result = movieService.offlineRecommend(uid);
        return getMovieRecommendResult(result);
    }

    @Override
    public HttpResult onlineRecommend(Integer uid) {
        HttpResult result = movieService.streamingRecommend(uid);
        return getMovieRecommendResult(result);
    }

    private HttpResult getMovieRecommendResult(HttpResult result) {
        if (result.getCode() == HttpResult.SUCCESS_CODE) {
            LinkedHashMap map = ((LinkedHashMap) ((ArrayList) result.getData()).get(0));
            ArrayList<LinkedHashMap> recommend = (ArrayList) map.get("recommend");
            List<Integer> movieIds = new ArrayList<>();
            for (LinkedHashMap linkedHashMap : recommend) {
                movieIds.add((Integer) linkedHashMap.get("mid"));
            }
            List<Movie> movies = movieMapper.selectByMovieIds(movieIds);
            movies.forEach(movie -> movie.setCoverUrl(imagePath + movie.getCoverUrl()));
            return HttpResult.success(movies);
        } else {
            return HttpResult.fail("服务调用失败");
        }
    }

    @Override
    public HttpResult mark(RatingParam param) {
        return movieService.mark(param);
    }

    @Override
    public HttpPageResult moviePage(MovieSearchParam param) {
        return movieService.moviePage(param);
    }
}
