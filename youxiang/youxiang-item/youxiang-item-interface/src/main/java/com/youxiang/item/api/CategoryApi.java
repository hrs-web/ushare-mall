package com.youxiang.item.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("category")
public interface CategoryApi {

    /**
     * 根据多个分类id查询分类名称
     * @param ids
     * @return
     */
    @GetMapping
    List<String> queryNamesByIds(@RequestParam("ids")List<Long> ids);
}
