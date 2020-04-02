package com.ccb.movie.bean.movie;

import lombok.Data;

@Data
public class Rating {

    private Long movieId;

    private Long userId;

    private Integer rating;
}
