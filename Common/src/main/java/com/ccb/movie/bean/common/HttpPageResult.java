package com.ccb.movie.bean.common;

public class HttpPageResult extends HttpResult {

    private PageInfo pageInfo;

    public HttpPageResult(Object data, PageInfo pageInfo) {
        super(HttpResult.SUCCESS_CODE, HttpResult.SUCCESS_MSG, data);
        this.pageInfo = pageInfo;
    }

    public static HttpPageResult success(Object data, PageInfo pageInfo) {
        return new HttpPageResult(data, pageInfo);
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
}
