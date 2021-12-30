package com.youxiang.item.service.impl;

import com.youxiang.item.mapper.CategoryMapper;
import com.youxiang.item.pojo.Category;
import com.youxiang.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public List<Category> queryCategoryByPid(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        return this.categoryMapper.select(category);
    }

    @Override
    public List<Category> queryBrandById(Long bid) {
        return this.categoryMapper.queryBrandById(bid);
    }
}
