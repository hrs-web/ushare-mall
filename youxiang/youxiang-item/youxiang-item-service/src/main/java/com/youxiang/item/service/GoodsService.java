package com.youxiang.item.service;

import com.youxiang.common.pojo.PageResult;
import com.youxiang.item.bo.SpuBo;
import com.youxiang.item.pojo.Sku;
import com.youxiang.item.pojo.SpuDetail;

import java.util.List;

public interface GoodsService {
    PageResult<SpuBo> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows);

    void saveGoods(SpuBo spuBo);

    SpuDetail querySpuDetailBySpuId(Long spuId);

    List<Sku> querySkuBySpuId(Long spuId);

    void updateGoods(SpuBo spuBo);

    void updateSaleable(Long spuId, Boolean saleable);

    void deleteGoods(Long spuId);
}
