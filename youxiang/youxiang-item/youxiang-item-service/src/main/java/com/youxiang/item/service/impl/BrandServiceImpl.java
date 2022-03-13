package com.youxiang.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.youxiang.common.pojo.PageResult;
import com.youxiang.item.mapper.BrandMapper;
import com.youxiang.item.pojo.Brand;
import com.youxiang.item.service.BrandService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandMapper brandMapper;

    @Override
    public PageResult<Brand> queryBrandByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        // 1.初始化Example对象
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        // 2.添加模糊查询(isNotBlank：排除空格参数)
        if (StringUtils.isNotBlank(key)){
            criteria.andLike("name","%"+key+"%").orEqualTo("letter",key);
        }
        // 3.添加分页
        PageHelper.startPage(page,rows);
        // 4.添加排序
        if (StringUtils.isNotBlank(sortBy)){
            example.setOrderByClause(sortBy+(desc ? " desc":" asc"));
        }
        // 5.调用mapper方法
        List<Brand> brands = this.brandMapper.selectByExample(example);
        PageInfo<Brand> info = new PageInfo<>(brands);

        return new PageResult<>(info.getTotal(),info.getList());
    }

    @Override
    @Transactional  // 添加事务
    public void saveBrand(Brand brand, List<Long> cids) {
        // 1.插入品牌表
        this.brandMapper.insertSelective(brand);  // insertSelective：只给有值的字段插入
        // 2.插入关联表
        cids.forEach(cid -> this.brandMapper.saveCategoryAndBrand(cid,brand.getId()));
    }

    @Override
    @Transactional
    public void updateBrand(Brand brand, List<Long> cids) {
        // 1.更新商品表
        this.brandMapper.updateByPrimaryKeySelective(brand);
        // 2.删除关联表
        this.brandMapper.deleteCategoryAndBrand(brand.getId());
        // 3.插入关联表
        cids.forEach(cid -> this.brandMapper.saveCategoryAndBrand(cid,brand.getId()));
    }

    @Override
    public void deleteBrand(Long id) {
        // 1.删除商品表
        this.brandMapper.deleteByPrimaryKey(id);
        // 2.删除关联表
        this.brandMapper.deleteCategoryAndBrand(id);
    }

    /**
     * 根据分类id查询品牌集合
     * @param cid
     * @return
     */
    @Override
    public List<Brand> queryBrandByCid(Long cid) {
        return this.brandMapper.selectBrandByCid(cid);
    }


}
