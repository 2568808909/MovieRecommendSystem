package com.ccb.movie.bean.movie.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Recommend implements Serializable {

    private Integer uid;

    private List<MovieRecommendRes> recommend;
}
