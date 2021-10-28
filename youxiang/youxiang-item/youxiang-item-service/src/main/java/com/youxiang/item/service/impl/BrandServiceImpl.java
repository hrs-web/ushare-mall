package com.youxiang.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.youxiang.common.pojo.PageResult;
import com.youxiang.item.mapper.BrandMapping;
import com.youxiang.item.pojo.Brand;
import com.youxiang.item.service.BrandService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandMapping brandMapping;

    @Override
    public PageResult<Brand> queryBrandByPage(String key, Integer page, Integer row, String sortBy, Boolean desc) {
        // 1.初始化Example对象
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        // 2.添加模糊查询(isNotBlank：排除空格参数)
        if (StringUtils.isNotBlank(key)){
            criteria.andLike("name","%"+key+"%").orEqualTo("letter",key);
        }
        // 3.添加分页
        PageHelper.startPage(page,row);
        // 4.添加排序
        if (StringUtils.isNotBlank(sortBy)){
            example.setOrderByClause(sortBy+(desc ? " desc":" asc"));
        }
        // 5.调用mapper方法
        List<Brand> brands = this.brandMapping.selectByExample(example);
        PageInfo<Brand> info = new PageInfo<>(brands);

        return new PageResult<>(info.getTotal(),info.getList());
    }
}
