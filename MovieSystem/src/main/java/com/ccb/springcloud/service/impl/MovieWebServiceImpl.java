package com.ccb.springcloud.service.impl;

import com.ccb.movie.bean.common.HttpResult;
import com.ccb.movie.bean.movie.vo.MovieSearchParam;
import com.ccb.movie.bean.movie.vo.RatingParam;
import com.ccb.movie.bean.movie.vo.Recommend;
import com.ccb.springcloud.feign.MovieService;
import com.ccb.springcloud.mapper.MovieMapper;
import com.ccb.springcloud.service.MovieWebService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public HttpResult offlineRecommend(Integer uid) {
        HttpResult result = movieService.offlineRecommend(uid);
        if (result.getCode() == HttpResult.SUCCESS_CODE) {
            LinkedHashMap map = ((LinkedHashMap) ((ArrayList) result.getData()).get(0));
            ArrayList<LinkedHashMap> recommend = (ArrayList) map.get("recommend");
            List<Integer> movies = new ArrayList<>();
            for (LinkedHashMap linkedHashMap : recommend) {
                movies.add((Integer) linkedHashMap.get("mid"));
            }
            return movies.size() != 0 ? HttpResult.success(movieMapper.selectByMovieIds(movies)) : HttpResult.success();
        } else {
            return HttpResult.fail("服务调用失败");
        }
    }

    @Override
    public HttpResult onlineRecommend(Integer uid) {
        HttpResult result = movieService.streamingRecommend(uid);
        if (result.getCode() == HttpResult.SUCCESS_CODE) {
            LinkedHashMap map = ((LinkedHashMap) ((ArrayList) result.getData()).get(0));
            ArrayList<LinkedHashMap> recommend = (ArrayList) map.get("recommend");
            List<Integer> movies = new ArrayList<>();
            for (LinkedHashMap linkedHashMap : recommend) {
                movies.add((Integer) linkedHashMap.get("mid"));
            }
            return movies.size() != 0 ? HttpResult.success(movieMapper.selectByMovieIds(movies)) : HttpResult.success();
        } else {
            return HttpResult.fail("服务调用失败");
        }
    }

    @Override
    public HttpResult mark(RatingParam param) {
        return movieService.mark(param);
    }

    @Override
    public HttpResult moviePage(MovieSearchParam param) {
        return movieService.moviePage(param);
    }
}
