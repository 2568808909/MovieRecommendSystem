package com.ccb.movie.bean.movie.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MovieRecommendRes implements Comparable<MovieRecommendRes> {

    private Integer mid;

    private Double score;

    @Override
    public int compareTo(MovieRecommendRes o) {
        return o.score.compareTo(this.score);
    }
}
