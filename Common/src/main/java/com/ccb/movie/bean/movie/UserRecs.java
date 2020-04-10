package com.ccb.movie.bean.movie;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

@Data
@ToString
@Table(name = "user_recs")
public class UserRecs {

    private Integer uid;

    private String recs;
}
