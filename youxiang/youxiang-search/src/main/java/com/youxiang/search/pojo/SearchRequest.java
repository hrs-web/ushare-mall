package com.youxiang.search.pojo;

import java.util.Map;

public class SearchRequest {
    private String key;  // 搜索条件

    private Integer page; // 当前页

    private Map<String,String> filter; // 过滤条件

    private static final Integer DEFAULT_SIZE = 20; // 默认的每页大小，固定值
    private static final Integer DEFAULT_PAGE = 1;  // 默认页数

    public Map<String, String> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, String> filter) {
        this.filter = filter;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPage() {
        if (page == null){
            return DEFAULT_PAGE;
        }
        return Math.max(DEFAULT_PAGE,page);
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize(){
        return DEFAULT_SIZE;
    }
}
