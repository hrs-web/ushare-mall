package com.youxiang.search.pojo;

import com.youxiang.common.pojo.PageResult;
import com.youxiang.item.pojo.Brand;

import java.util.List;
import java.util.Map;

public class SearchResult extends PageResult<Goods> {
    private List<Map<String,Object>> categories;
    private List<Brand> brands;
    private List<Map<String,Object>> specs;

    public SearchResult(){
    }

    public SearchResult(List<Map<String,Object>> categories,List<Brand> brands){
        this.categories = categories;
        this.brands = brands;
    }

    public SearchResult(Long total,List<Goods> goods,List<Map<String,Object>> categories,List<Brand> brands){
        super(total,goods);
        this.categories = categories;
        this.brands = brands;
    }

    public SearchResult(Long total,Integer totalPage,List<Goods> goods,List<Map<String,Object>> categories,List<Brand> brands){
        super(total,totalPage,goods);
        this.categories = categories;
        this.brands = brands;
    }

    public SearchResult(Long total,Integer totalPage,List<Goods> goods,List<Map<String,Object>> categories,List<Brand> brands,List<Map<String,Object>> specs){
        super(total,totalPage,goods);
        this.specs = specs;
        this.categories = categories;
        this.brands = brands;
    }

    public List<Map<String, Object>> getSpecs() {
        return specs;
    }

    public void setSpecs(List<Map<String, Object>> specs) {
        this.specs = specs;
    }

    public List<Map<String, Object>> getCategories() {
        return categories;
    }

    public void setCategories(List<Map<String, Object>> categories) {
        this.categories = categories;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }
}
