package com.youxiang.goods.listener;

import com.youxiang.goods.service.GoodsHtmlService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoodsListener {
    @Autowired
    private GoodsHtmlService goodsHtmlService;

    /**
     * 处理insert和update的消息
     * @param spuId
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "YOUXIANG.WEB.SAVE.QUEUE",durable = "true"),
            exchange = @Exchange(
                    value = "YOUXIANG.ITEM.EXCHANGE",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"item.insert","item.update"}
            ))
    public void saveListener(Long spuId){
        if (spuId == null){
            return;
        }
        this.goodsHtmlService.createHtml(spuId);
    }

    /**
     * 处理delete的消息
     * @param spuId
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "YOUXIANG.WEB.DELETE.QUEUE",durable = "true"),
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
        this.goodsHtmlService.deleteHtml(spuId);
    }
}
