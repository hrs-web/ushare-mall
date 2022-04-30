package com.youxiang.goods.controller;

import com.youxiang.goods.service.GoodsHtmlService;
import com.youxiang.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("item")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsHtmlService goodsHtmlService;

    @GetMapping("{spuId}.html")
    public String toItemPage(Model model, @PathVariable("spuId")Long spuId){
        // 加载所需数据
        Map<String, Object> map = this.goodsService.loadData(spuId);
        // 页面静态化
        model.addAllAttributes(map);

        // 页面静态化
        this.goodsHtmlService.asynExcute(spuId);

        return "item";
    }
}
