package com.youxiang.goods.service;

import com.youxiang.goods.client.BrandClient;
import com.youxiang.goods.client.CategoryClient;
import com.youxiang.goods.client.GoodsClient;
import com.youxiang.goods.client.SpecificationClient;
import com.youxiang.item.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GoodsService {
    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecificationClient specificationClient;

    public Map<String,Object> loadData(Long spuId){
        Map<String,Object> model = new HashMap<>();

        // 根据id查询spu对象
        Spu spu = this.goodsClient.querySpuById(spuId);

        // 根据spuId查询spuDetail
        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spuId);

        // 根据spuId查询sku集合
        List<Sku> skus = this.goodsClient.querySkusBySpuId(spuId);

        // 查询分类
        List<Long> cids = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
        List<String> names = this.categoryClient.queryNamesByIds(cids);
        List<Map<String,Object>> categories = new ArrayList<>();
        for (int i = 0;i < cids.size(); i++){
            Map<String, Object> categoryMap = new HashMap<>();
            categoryMap.put("id",cids.get(i));
            categoryMap.put("name",names.get(i));
            categories.add(categoryMap);
        }

        // 查询品牌
        Brand brand = this.brandClient.queryBrandById(spu.getBrandId());

        // 查询规格参数组及组内规格参数
        List<SpecGroup> groups = this.specificationClient.queryGroupWithParam(spu.getCid3());

        //查询特殊规格参数
        List<SpecParam> params = this.specificationClient.queryParams(null, spu.getCid3(), false, null);
        Map<Long, String> paramMap = new HashMap<>();
        params.forEach(param -> {
            paramMap.put(param.getId(),param.getName());
        });


        model.put("spu",spu);
        model.put("spuDetail",spuDetail);
        model.put("skus",skus);
        model.put("categories",categories);
        model.put("brand",brand);
        model.put("groups",groups);
        model.put("paramMap",paramMap);
        return model;
    }
}
