package com.youxiang.item.service;

import com.youxiang.item.pojo.SpecGroup;
import com.youxiang.item.pojo.SpecParam;

import java.util.List;

public interface SpecificationService {
    List<SpecGroup> queryGroupsByCid(Long cid);

    List<SpecParam> queryParams(Long gid);

    void saveGroup(SpecGroup specGroup);

    void updateGroup(SpecGroup specGroup);

    void deleteGroup(Long id);

    void saveParam(SpecParam specParam);

    void updateParam(SpecParam specParam);

    void deleteParam(Long id);
}
