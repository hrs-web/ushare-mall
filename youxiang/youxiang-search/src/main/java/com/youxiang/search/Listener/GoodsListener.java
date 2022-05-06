package com.youxiang.search.Listener;

import com.youxiang.search.service.SearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GoodsListener {
    @Autowired
    private SearchService searchService;

    /**
     * 处理insert和update的消息
     * @param spuId
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "YOUXIANG.SEARCH.SAVE.QUEUE",durable = "true"),
            exchange = @Exchange(
                    value = "YOUXIANG.ITEM.EXCHANGE",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"item.insert","item.update"}
    ))
    public void saveListener(Long spuId) throws IOException {
        if (spuId == null){
            return;
        }
        // 创建或更新索引
        this.searchService.saveIndex(spuId);
    }

    /**
     * 处理delete的消息
     * @param spuId
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "YOUXIANG.SEARCH.DELETE.QUEUE",durable = "true"),
            exchange = @Exchange(
                    value = "YOUXIANG.ITEM.EXCHANGE",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"item.delete"}
    ))
    public void deleteListener(Long spuId){
        if (spuId == null){
            return;
        }
        this.searchService.deleteIndex(spuId);
    }
}
