package com.youxiang.item.controller;

import com.youxiang.item.pojo.Category;
import com.youxiang.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 根据父类目id查所有子节点
     * @param pid
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<Category>> queryCategoryByPid(@RequestParam(value = "pid", defaultValue = "0") Long pid) {
        if (pid == null || pid < 0){
            // 响应400
            // return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            // return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            return ResponseEntity.badRequest().build();
        }
        // 执行查询，获取结果集
        List<Category> categories = this.categoryService.queryCategoryByPid(pid);
        if (CollectionUtils.isEmpty(categories)){
            // 响应404
            // return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return ResponseEntity.notFound().build();
        }
        // 响应200
        return ResponseEntity.ok(categories);
    }

    /**
     * 通过品牌id查询品牌分类
     * @param bid
     * @return
     */
    @GetMapping("bid/{bid}")
    public ResponseEntity<List<Category>> queryBrandById(@PathVariable("bid") Long bid){
        List<Category> list = categoryService.queryBrandById(bid);
        if (list == null || list.size() < 1){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(list);
    }
}
