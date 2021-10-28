package com.youxiang.item.service;

import com.youxiang.common.pojo.PageResult;
import com.youxiang.item.pojo.Brand;

public interface BrandService {
    /**
     * 分页查询品牌
     * @param key
     * @param page
     * @param row
     * @param sortBy
     * @param desc
     * @return
     */
    PageResult<Brand> queryBrandByPage(String key,Integer page,Integer row,String sortBy,Boolean desc);
}
