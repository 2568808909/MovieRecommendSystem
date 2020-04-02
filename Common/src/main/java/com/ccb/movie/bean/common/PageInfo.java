package com.ccb.movie.bean.common;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PageInfo {

    private Object data;

    private Integer totalCount;

    private Integer pageSize;

    private boolean lastPage;
}
