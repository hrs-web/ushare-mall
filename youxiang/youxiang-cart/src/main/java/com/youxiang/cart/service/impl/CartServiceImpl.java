package com.youxiang.cart.service.impl;

import com.youxiang.auth.pojo.UserInfo;
import com.youxiang.cart.client.GoodsClient;
import com.youxiang.cart.intercept.LoginInterceptor;
import com.youxiang.cart.pojo.Cart;
import com.youxiang.cart.service.CartService;
import com.youxiang.common.utils.JsonUtils;
import com.youxiang.item.pojo.Sku;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private GoodsClient goodsClient;

    static final String KEY_PREFIX = "youxiang:cart:uid:";

    public void addCart(Cart cart) {
        // 获取登录用户
        UserInfo userInfo = LoginInterceptor.getLoginUser();
        // Redis的key
        String key = KEY_PREFIX + userInfo.getId();
        // 获取hash操作对象
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        // 查询是否存在
        Long skuId = cart.getSkuId();
        Integer num = cart.getNum();
        Boolean bool = hashOps.hasKey(skuId.toString());
        if (bool){
            // 购物车已存在该商品，更商品数量
            String json = hashOps.get(skuId.toString()).toString();
            cart = JsonUtils.parse(json, Cart.class);
            cart.setNum(cart.getNum() + num);
        }else {
            // 不存在，新增购物车数据
            cart.setUserId(userInfo.getId());
            // 其他商品信息，需要查询商品服务
            Sku sku = this.goodsClient.querySkuById(skuId);
            cart.setImage(StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(),",")[0]);
            cart.setPrice(sku.getPrice());
            cart.setTitle(sku.getTitle());
            cart.setOwnSpec(sku.getOwnSpec());
        }
        // 将购物车数据写入redis
        hashOps.put(cart.getSkuId().toString(),JsonUtils.serialize(cart));
    }

    /**
     * 查询购物车
     * @return
     */
    @Override
    public List<Cart> queryCartList() {
        // 获取登录用户
        UserInfo userInfo = LoginInterceptor.getLoginUser();
        // Redis的key
        String key = KEY_PREFIX + userInfo.getId();
        if (!this.redisTemplate.hasKey(key)){
            // 不存在直接返回
            return null;
        }
        // 获取hash操作对象
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        List<Object> cartJsons = hashOps.values();
        // 判断是否有数据
        if (CollectionUtils.isEmpty(cartJsons)){
            return null;
        }
        return cartJsons.stream().map(cartJson -> JsonUtils.parse(cartJson.toString(),Cart.class)).collect(Collectors.toList());
    }

    /**
     * 更新购物车数量
     * @param cart
     */
    @Override
    public void updateCart(Cart cart) {
        // 获取登录信息
        UserInfo userInfo = LoginInterceptor.getLoginUser();
        String key = KEY_PREFIX + userInfo.getId();
        // 获取hash操作对象
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        // 获取购物车信息
        String cartJson = hashOps.get(cart.getSkuId().toString()).toString();
        Cart c = JsonUtils.parse(cartJson, Cart.class);
        // 更新数量
        c.setNum(cart.getNum());
        // 写入购物车
        hashOps.put(cart.getSkuId().toString(),JsonUtils.serialize(c));
    }

    /**
     * 删除购物车
     * @param skuId
     */
    @Override
    public void deleteCart(String skuId) {
        // 获取登录信息
        UserInfo userInfo = LoginInterceptor.getLoginUser();
        String key = KEY_PREFIX + userInfo.getId();
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        hashOps.delete(skuId);
    }
}
