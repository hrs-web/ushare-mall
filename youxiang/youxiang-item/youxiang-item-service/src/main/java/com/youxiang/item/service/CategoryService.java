package com.youxiang.item.service;

import com.youxiang.item.pojo.Category;

import java.util.List;

public interface CategoryService {
    /**
     * 根据父id查找子目录
     * @param id
     * @return
     */
    List<Category> queryCategoryByPid(Long id);

    List<Category> queryBrandById(Long bid);

    List<String> queryNamesByIds(List<Long> asList);
}
