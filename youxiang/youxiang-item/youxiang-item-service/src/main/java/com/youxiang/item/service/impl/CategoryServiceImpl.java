package com.youxiang.item.service.impl;

import com.youxiang.item.mapper.CategoryMapper;
import com.youxiang.item.pojo.Category;
import com.youxiang.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 根据多个分类id查询分类名称
     * @param ids
     * @return
     */
    @Override
    public List<String> queryNamesByIds(List<Long> ids) {
        List<Category> categories = this.categoryMapper.selectByIdList(ids);
        // 把List<category>转换为List<String>
        return categories.stream().map(category -> category.getName()).collect(Collectors.toList());
    }
}
