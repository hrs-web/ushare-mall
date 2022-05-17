package com.youxiang.item.api;

import com.youxiang.common.pojo.PageResult;
import com.youxiang.item.bo.SpuBo;
import com.youxiang.item.pojo.Sku;
import com.youxiang.item.pojo.Spu;
import com.youxiang.item.pojo.SpuDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface GoodsApi {
    /**
     * 分页查询spu
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @GetMapping("spu/page")
    PageResult<SpuBo> querySpuByPage(
            @RequestParam(value = "key",required = false)String key,
            @RequestParam(value = "saleable",required = false)Boolean saleable,
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "rows",defaultValue = "5")Integer rows
    );

    /**
     * 根据spu的id查询spuDetail(商品详情)
     * @param spuId
     * @return
     */
    @GetMapping("spu/detail/{spuId}")
    SpuDetail querySpuDetailBySpuId(@PathVariable("spuId")Long spuId);

    /**
     * 根据spu的id查询sku集合
     * @param spuId
     * @return
     */
    @GetMapping("sku/list")
    List<Sku> querySkusBySpuId(@RequestParam("id")Long spuId);

    /**
     * 根据spu的id查询spu
     * @param id
     * @return
     */
    @GetMapping("spu/{id}")
    Spu querySpuById(@PathVariable("id")Long id);

    /**
     * 根据skuId查询sku
     * @param id
     * @return
     */
    @GetMapping("sku/{id}")
    Sku querySkuById(@PathVariable("id") Long id);
}
