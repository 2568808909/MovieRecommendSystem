package com.ccb.movie.test;

import com.ccb.movie.bean.movie.Rating;
import com.ccb.movie.service.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MovieManagementTest {

    @Autowired
    private MovieService movieService;

    @Test
    public void mark() {
        Rating rating = new Rating();
        rating.setMovieId(1561L);
        rating.setUserId(169L);
        rating.setRating(5);
        movieService.mark(rating);
    }
}
