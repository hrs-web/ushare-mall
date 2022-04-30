package com.youxiang.search.client;

import com.youxiang.YouxiangSearchApplication;
import com.youxiang.common.pojo.PageResult;
import com.youxiang.item.bo.SpuBo;
import com.youxiang.search.pojo.Goods;
import com.youxiang.search.repository.GoodsRepository;
import com.youxiang.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = YouxiangSearchApplication.class)
public class ElasticsearchTest {
    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SearchService searchService;

    @Autowired
    private GoodsRepository goodsRepository;

    @Test
    public void createIndex(){
        // 创建索引库，以及映射
//        this.templates.createIndex(Goods.class);
//        this.templates.putMapping(Goods.class);

        Integer page = 1;
        Integer rows = 100;

        do {
            // 分批查询spu
            PageResult<SpuBo> result = this.goodsClient.querySpuByPage(null, true, page, rows);
            List<SpuBo> items = result.getItems();
            // List<SpuBo> ==> List<goods>
            List<Goods> goodsList = items.stream().map(spuBo -> {
                try {
                    return this.searchService.buildGoods(spuBo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList());
            this.goodsRepository.saveAll(goodsList);

            page++;
            rows = items.size();
        }while (rows == 100);
    }
}
