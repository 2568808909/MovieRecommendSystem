package com.ccb.movie.bean.movie.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OfflineRecommend {

    private Integer uid;

    private List<MovieRecommendRes> recommend;
}
