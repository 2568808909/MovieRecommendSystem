package com.ccb.movie.bean.movie;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;

@Data
@ToString
@Table(name = "stream_recs")
public class StreamRecs {

    private Integer uid;

    private String recs;
}
