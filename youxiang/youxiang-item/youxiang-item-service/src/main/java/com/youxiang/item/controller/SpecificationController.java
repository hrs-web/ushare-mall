package com.youxiang.item.controller;

import com.youxiang.item.pojo.SpecGroup;
import com.youxiang.item.pojo.SpecParam;
import com.youxiang.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecificationController {
    @Autowired
    private SpecificationService specificationService;

    /**
     * 根据分类id查询分组
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupsByCid(@PathVariable("cid") Long cid){
        List<SpecGroup> groups = this.specificationService.queryGroupsByCid(cid);
        if (CollectionUtils.isEmpty(groups)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(groups);
    }

    /**
     * 根据条件查询规格参数
     * @param gid
     * @return
     */
    @GetMapping("params")
    private ResponseEntity<List<SpecParam>> queryParams(
            @RequestParam(value = "gid",required = false) Long gid,
            @RequestParam(value = "cid",required = false) Long cid,
            @RequestParam(value = "generic",required = false) Boolean generic,
            @RequestParam(value = "searching",required = false) Boolean searching
    ){
        List<SpecParam> params = this.specificationService.queryParams(gid,cid,generic,searching);
        if (CollectionUtils.isEmpty(params)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(params);
    }

    /**
     * 新增规格分组
     * @param specGroup
     * @return
     */
    @PostMapping("group")
    private ResponseEntity<Void> saveGroup(@RequestBody SpecGroup specGroup){
        this.specificationService.saveGroup(specGroup);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 修改规格分组
     * @param specGroup
     * @return
     */
    @PutMapping("group")
    private ResponseEntity<Void> updateGroup(@RequestBody SpecGroup specGroup){
        this.specificationService.updateGroup(specGroup);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 删除规格分组
     * @param id
     * @return
     */
    @DeleteMapping("group/{id}")
    private ResponseEntity<Void> deleteGroup(@PathVariable("id")Long id){
        this.specificationService.deleteGroup(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 新增规格参数
     * @param specParam
     * @return
     */
    @PostMapping("param")
    private ResponseEntity<Void> saveParam(@RequestBody SpecParam specParam){
        this.specificationService.saveParam(specParam);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 修改规格参数
     * @param specParam
     * @return
     */
    @PutMapping("param")
    private ResponseEntity<Void> updateParam(@RequestBody SpecParam specParam){
        this.specificationService.updateParam(specParam);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 删除规格参数
     * @param id
     * @return
     */
    @DeleteMapping("param/{id}")
    private ResponseEntity<Void> deleteParam(@PathVariable("id")Long id){
        this.specificationService.deleteParam(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
