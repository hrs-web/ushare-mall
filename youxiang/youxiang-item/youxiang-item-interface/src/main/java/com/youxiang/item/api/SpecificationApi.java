package com.youxiang.item.api;

import com.youxiang.item.pojo.SpecGroup;
import com.youxiang.item.pojo.SpecParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("spec")
public interface SpecificationApi {

    /**
     * 根据条件查询规格参数
     * @param gid
     * @return
     */
    @GetMapping("params")
    List<SpecParam> queryParams(
            @RequestParam(value = "gid",required = false) Long gid,
            @RequestParam(value = "cid",required = false) Long cid,
            @RequestParam(value = "generic",required = false) Boolean generic,
            @RequestParam(value = "searching",required = false) Boolean searching
    );

    /**
     * 查询规格参数组及组内的规格参数
     * @param cid
     * @return
     */
    @GetMapping("{cid}")
    List<SpecGroup> queryGroupWithParam(@PathVariable("cid")Long cid);
}
