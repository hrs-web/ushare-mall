package com.youxiang.item.mapper;

import com.youxiang.item.pojo.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand> {
    @Insert("INSERT INTO tb_category_brand (category_id,brand_id) VALUES (#{cid},#{bid})")
    void saveCategoryAndBrand(@Param("cid") Long cid, @Param("bid") Long bid);

    @Delete("DELETE FROM tb_category_brand WHERE brand_id = #{id}")
    void deleteCategoryAndBrand(Long cid);

    @Select("SELECT * FROM tb_brand as a INNER JOIN tb_category_brand as b ON a.id = b.brand_id WHERE b.category_id = #{cid}")
    List<Brand> selectBrandByCid(Long cid);
}
