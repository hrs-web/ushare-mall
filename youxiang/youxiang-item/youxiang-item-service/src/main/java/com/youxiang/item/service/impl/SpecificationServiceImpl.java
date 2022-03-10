package com.youxiang.item.service.impl;

import com.youxiang.item.mapper.SpecGroupMapper;
import com.youxiang.item.mapper.SpecParamsMapper;
import com.youxiang.item.pojo.SpecGroup;
import com.youxiang.item.pojo.SpecParam;
import com.youxiang.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecificationServiceImpl implements SpecificationService {
    @Autowired
    private SpecGroupMapper specGroupMapper;
    @Autowired
    private SpecParamsMapper specParamsMapper;

    /**
     * 根据分类id查询规格参数组
     * @param cid
     * @return
     */
    @Override
    public List<SpecGroup> queryGroupsByCid(Long cid) {
        SpecGroup record = new SpecGroup();
        record.setCid(cid);
        return this.specGroupMapper.select(record);
    }

    /**
     * 根据条件查询规格参数
     * @param gid
     * @return
     */
    @Override
    public List<SpecParam> queryParams(Long gid) {
        SpecParam param = new SpecParam();
        param.setGroupId(gid);
        return this.specParamsMapper.select(param);
    }

    /**
     * 新增规格分组
     * @param specGroup
     */
    @Override
    public void saveGroup(SpecGroup specGroup) {
        this.specGroupMapper.insertSelective(specGroup);
    }

    /**
     * 修改规格分组
     * @param specGroup
     */
    @Override
    public void updateGroup(SpecGroup specGroup) {
        this.specGroupMapper.updateByPrimaryKeySelective(specGroup);
    }

    /**
     * 删除规格分组
     * @param id
     */
    @Override
    public void deleteGroup(Long id) {
        // 此处删除规格参数组，并没有删除参数组下的规格参数
        this.specGroupMapper.deleteByPrimaryKey(id);
    }

    /**
     * 新增规格参数
     * @param specParam
     */
    @Override
    public void saveParam(SpecParam specParam) {
        this.specParamsMapper.insertSelective(specParam);
    }

    /**
     * 修改规格参数
     * @param specParam
     */
    @Override
    public void updateParam(SpecParam specParam) {
        this.specParamsMapper.updateByPrimaryKeySelective(specParam);
    }

    /**
     * 删除规格参数
     * @param id
     */
    @Override
    public void deleteParam(Long id) {
        this.specParamsMapper.deleteByPrimaryKey(id);
    }
}
