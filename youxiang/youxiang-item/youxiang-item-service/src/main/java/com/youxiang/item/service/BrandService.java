package com.youxiang.item.service;

import com.youxiang.common.pojo.PageResult;
import com.youxiang.item.pojo.Brand;

import java.util.List;

public interface BrandService {
    /**
     * 分页查询品牌
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    PageResult<Brand> queryBrandByPage(String key,Integer page,Integer rows,String sortBy,Boolean desc);

    /**
     * 新增品牌
     * @param brand
     * @param cids
     */
    void saveBrand(Brand brand, List<Long> cids);

    void updateBrand(Brand brand, List<Long> cids);

    void deleteBrand(Long id);

    List<Brand> queryBrandByCid(Long cid);

    Brand queryBrandById(Long id);
}
