package com.ccb.movie.service.impl;

import com.ccb.movie.bean.common.HttpPageResult;
import com.ccb.movie.bean.common.HttpResult;
import com.ccb.movie.bean.common.PageInfo;
import com.ccb.movie.bean.movie.Movie;
import com.ccb.movie.bean.movie.Rating;
import com.ccb.movie.bean.movie.StreamRecs;
import com.ccb.movie.bean.movie.UserRecs;
import com.ccb.movie.bean.movie.vo.MovieRecommendRes;
import com.ccb.movie.bean.movie.vo.Recommend;
import com.ccb.movie.exception.BizException;
import com.ccb.movie.feign.UserService;
import com.ccb.movie.mapper.MovieMapper;
import com.ccb.movie.mapper.RatingMapper;
import com.ccb.movie.mapper.StreamRecsMapper;
import com.ccb.movie.mapper.UserRecsMapper;
import com.ccb.movie.service.MovieService;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private RatingMapper ratingMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRecsMapper userRecsMapper;

    @Autowired
    private Producer<String, String> producer;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private StreamRecsMapper streamRecsMapper;

    @Value("${image.path}")
    private String imagePath;

    @Override
    public void mark(Rating rating) {
        try {
            Long mid = rating.getMovieId();
            if (movieMapper.selectByPrimaryKey(mid) == null) {
                throw new BizException("您所评价的电影不存在: " + mid);
            }
            Long uid = rating.getUserId();
            if (userService.getUserById(uid).getData() == null) {
                throw new BizException("用户不存在: " + uid);
            }
            Date now = new Date();
            rating.setCreatedTime(now);
            rating.setUpdatedTime(now);
            ratingMapper.insert(rating);
            producer.send(new ProducerRecord<>("movie-rating", uid + "|" + mid + "|" + rating.getRating() + "|" + now.getTime()));
            redisTemplate.boundListOps("uid:" + uid).rightPush(mid + ":" + rating.getRating());
        } catch (DuplicateKeyException e) {
            throw new BizException("您已经对该电影评过分了哦，不能多次评分哦！");
        }
    }

    @Override
    public HttpPageResult moviePage(Movie movie, Integer pageNum, Integer pageSize) {
        long count = movieMapper.count(movie);
        Integer pageStart = (pageNum - 1) * pageSize;
        List<Movie> movies = movieMapper.moviePage(movie, pageStart, pageSize);
        PageInfo pageInfo = new PageInfo();
        pageInfo.setLastPage(pageStart + pageSize >= count);
        pageInfo.setTotalCount(count);
        pageInfo.setPageSize(pageSize);
        movies.forEach(m -> m.setCoverUrl(imagePath + m.getCoverUrl()));
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

    //    select * from rating where user_id=2 and movie_id=3208
//    select * from rating where movie_id=3208
//    select * from rating where user_id=2
    @Override
    public HttpResult offlineRecommend(Integer uid) {
        UserRecs userRecs = new UserRecs();
        userRecs.setUid(uid);
        List<UserRecs> res = userRecsMapper.select(userRecs);
        List<Recommend> recommends = res.stream().map(ur -> {
            String[] movieAndScore = ur.getRecs().split("\\|");
            List<MovieRecommendRes> recommend = Arrays.stream(movieAndScore).map(str -> {
                String[] ms = str.split(":");
                return new MovieRecommendRes(Integer.valueOf(ms[0]), Double.valueOf(ms[1]));
            }).collect(Collectors.toList());
            return new Recommend(ur.getUid(), recommend);
        }).collect(Collectors.toList());
        return HttpResult.success(recommends);
    }

    @Override
    public HttpResult streamingRecommend(Integer uid) {
        StreamRecs streamRecs = new StreamRecs();
        streamRecs.setUid(uid);
        List<StreamRecs> res = streamRecsMapper.select(streamRecs);
        List<Recommend> recommends = res.stream().map(ur -> {
            String[] movieAndScore = ur.getRecs().split("\\|");
            List<MovieRecommendRes> recommend = Arrays.stream(movieAndScore).map(str -> {
                String[] ms = str.split(":");
                return new MovieRecommendRes(Integer.valueOf(ms[0]), Double.valueOf(ms[1]));
            }).sorted().collect(Collectors.toList());
            return new Recommend(ur.getUid(), recommend);
        }).collect(Collectors.toList());
        return HttpResult.success(recommends);
    }
}
