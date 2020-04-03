package com.ccb.movie.bean.common;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class PageInfo implements Serializable {
    private long totalCount;

    private Integer pageSize;

    private boolean lastPage;
}
