package com.ccb.movie.bean.common;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PageInfo {
    private long totalCount;

    private Integer pageSize;

    private boolean lastPage;
}
