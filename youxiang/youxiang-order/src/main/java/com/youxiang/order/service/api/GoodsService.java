package com.youxiang.order.service.api;

import com.youxiang.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "yx-gateway", path = "/api/item")
public interface GoodsService extends GoodsApi {
}
