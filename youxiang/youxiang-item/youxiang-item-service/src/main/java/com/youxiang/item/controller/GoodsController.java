package com.youxiang.item.controller;

import com.youxiang.common.pojo.PageResult;
import com.youxiang.item.bo.SpuBo;
import com.youxiang.item.pojo.Sku;
import com.youxiang.item.pojo.SpuDetail;
import com.youxiang.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;


@RestController
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    /**
     * 分页查询spu
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>> querySpuByPage(
            @RequestParam(value = "key",required = false)String key,
            @RequestParam(value = "saleable",required = false)Boolean saleable,
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "rows",defaultValue = "5")Integer rows
    ){
        PageResult<SpuBo> result = this.goodsService.querySpuByPage(key,saleable,page,rows);
        if (CollectionUtils.isEmpty(result.getItems())){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 保存商品
     * @param spuBo
     * @return
     */
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spuBo){
        this.goodsService.saveGoods(spuBo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据spu的id查询spuDetail(商品详情)
     * @param spuId
     * @return
     */
    @GetMapping("spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable("spuId")Long spuId){
        SpuDetail spuDetail = this.goodsService.querySpuDetailBySpuId(spuId);
        if (spuDetail == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spuDetail);
    }

    /**
     * 根据spu的id查询sku集合
     * @param spuId
     * @return
     */
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@PathParam("spuId")Long spuId){
        List<Sku> skus = this.goodsService.querySkuBySpuId(spuId);
        if (CollectionUtils.isEmpty(skus)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(skus);
    }

    /**
     * 更新商品信息
     * @param spuBo
     * @return
     */
    @PutMapping("goods")
    public ResponseEntity<Void> updateGoods(@RequestBody SpuBo spuBo){
        this.goodsService.updateGoods(spuBo);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 上下架操作
     * @param spuId
     * @param saleable
     * @return
     */
    @GetMapping("spu/saleable")
    public ResponseEntity<Void> updateSaleable(@PathParam("spuId")Long spuId,@PathParam("saleable")Boolean saleable){
        this.goodsService.updateSaleable(spuId,saleable);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    /**
     * 删除商品
     * @param spuId
     * @return
     */
    @DeleteMapping("spu/goods/{spuId}")
    public ResponseEntity<Void> deleteGoods(@PathVariable("spuId")Long spuId){
        this.goodsService.deleteGoods(spuId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
