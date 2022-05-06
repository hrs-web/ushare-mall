package com.youxiang.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.youxiang.common.pojo.PageResult;
import com.youxiang.item.bo.SpuBo;
import com.youxiang.item.mapper.*;
import com.youxiang.item.pojo.*;
import com.youxiang.item.service.CategoryService;
import com.youxiang.item.service.GoodsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 分页查询spu
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @Override
    public PageResult<SpuBo> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows) {
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        // 1.添加模糊查询
        if (StringUtils.isNotBlank(key)){
            criteria.andLike("title","%"+key+"%");
        }

        // 2.添加是否上下架过滤
        if (saleable != null){
            criteria.andEqualTo("saleable",saleable);
        }

        criteria.andEqualTo("valid",true);

        // 3.添加分页
        PageHelper.startPage(page,rows);

        // 4.执行查询
        List<Spu> spus = this.spuMapper.selectByExample(example);
        PageInfo<Spu> pageInfo = new PageInfo<>(spus);

        // 5.将List<Spu>转换为List<SpuBo>
        List<SpuBo> spuBos = spus.stream().map(spu -> {
            SpuBo spuBo = new SpuBo();
            // 把spu所有属性copy给spuBo
            BeanUtils.copyProperties(spu, spuBo);
            // 设置品牌名称和分类名称
            Brand brand = this.brandMapper.selectByPrimaryKey(spu.getBrandId());
            spuBo.setBname(brand.getName());
            List<String> names = this.categoryService.queryNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            spuBo.setCname(StringUtils.join(names, "/"));
            return spuBo;
        }).collect(Collectors.toList());

        // 6.返回分页结果
        return new PageResult<>(pageInfo.getTotal(),spuBos);
    }

    /**
     * 新增商品
     * @param spuBo
     */
    @Transactional
    public void saveGoods(SpuBo spuBo) {
        // 新增spu
        // 设置默认字段
        spuBo.setId(null);
        spuBo.setSaleable(true);
        spuBo.setValid(true);
        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(spuBo.getCreateTime());
        this.spuMapper.insertSelective(spuBo);

        // 新增spuDetail
        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetail.setSpuId(spuBo.getId());
        this.spuDetailMapper.insertSelective(spuDetail);

        // 新增sku和stock
        saveSkuAndStock(spuBo);

        // 发送insert消息给rabbitmq队列
        sendMessage("insert",spuBo.getId());
    }

    /**
     * 方法抽取
     * 新增sku和stock
     * @param spuBo
     * @return
     */
    private void saveSkuAndStock(SpuBo spuBo){
        List<Sku> skus = spuBo.getSkus();
        skus.forEach(sku -> {
            // 新增sku
            sku.setId(null);
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            this.skuMapper.insertSelective(sku);

            // 新增stock
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insertSelective(stock);
        });
    }

    /**
     * 根据spuId查询spuDetail(商品详情)
     * @param spuId
     * @return
     */
    @Override
    public SpuDetail querySpuDetailBySpuId(Long spuId) {
        return this.spuDetailMapper.selectByPrimaryKey(spuId);
    }

    /**
     * 根据spu的id查询sku集合
     * @param spuId
     * @return
     */
    @Override
    public List<Sku> querySkusBySpuId(Long spuId) {
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skus = this.skuMapper.select(sku);
        skus.forEach(s -> {
            Stock stock = this.stockMapper.selectByPrimaryKey(s.getId());
            s.setStock(stock.getStock());
        });
        return skus;
    }

    /**
     * 更新商品
     * @param spuBo
     */
    @Override
    @Transactional
    public void updateGoods(SpuBo spuBo) {
        Sku sku = new Sku();
        sku.setSpuId(spuBo.getId());

        // 删除stock
        List<Sku> skus = this.skuMapper.select(sku);
        skus.forEach(s -> {
            this.stockMapper.deleteByPrimaryKey(s.getId());
        });

        // 删除sku
        this.skuMapper.delete(sku);

        // 新增sku和stock
        saveSkuAndStock(spuBo);

        // 更新spu
        spuBo.setCreateTime(null);
        spuBo.setLastUpdateTime(new Date());
        this.spuMapper.updateByPrimaryKeySelective(spuBo);

        // 更新spuDetail
        SpuDetail spuDetail = spuBo.getSpuDetail();
        this.spuDetailMapper.updateByPrimaryKeySelective(spuDetail);
        // 发送update消息给rabbitmq队列
        sendMessage("update",spuBo.getId());
    }

    /**
     * 上下架操作
     * @param spuId
     * @param saleable
     */
    @Override
    public void updateSaleable(Long spuId, Boolean saleable) {
        Spu spu = new Spu();
        spu.setId(spuId);
        spu.setSaleable(!saleable);
        this.spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 删除商品
     * @param spuId
     */
    @Override
    public void deleteGoods(Long spuId) {
        Spu spu = new Spu();
        spu.setId(spuId);
        spu.setValid(false);
        this.spuMapper.updateByPrimaryKeySelective(spu);
        // 发送delete消息给rabbitmq队列
        sendMessage("delete",spuId);
    }

    /**
     * 根据spu的id查询spu
     * @param id
     * @return
     */
    @Override
    public Spu querySpuById(Long id) {
        return this.spuMapper.selectByPrimaryKey(id);
    }

    /**
     *发送信息到mq
     * @param type
     * @param spuId
     */
    private void sendMessage(String type,Long spuId){
        try {
            this.amqpTemplate.convertAndSend("item." + type,spuId);
        }catch (AmqpException e){
            e.printStackTrace();
        }

    }
}
