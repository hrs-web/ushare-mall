package com.youxiang.sms.listener;

import com.youxiang.sms.config.SmsProperties;
import com.youxiang.sms.utils.SmsUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SmsListener {
    @Autowired
    private SmsProperties prop;
    @Autowired
    private SmsUtils smsUtils;

    /**
     * 监听sms.verify队列，发送验证信息
     * @param map
     * @throws Exception
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "YOUXIANG.SMS.VERIFY.QUEUE",durable = "true"),
            exchange = @Exchange(value = "YOUXIANG.SMS.EXCHANGE",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"sms.verify"}
    ))
    public void listen(Map<String,String> map) throws Exception {
        if (CollectionUtils.isEmpty(map)){
            return;
        }
        String phone = map.get("phone");
        String code = map.get("code");
        if (StringUtils.isNotBlank(phone) && StringUtils.isNotBlank(code)){
            this.smsUtils.sendSms(phone,code,this.prop.getSignName(),this.prop.getTemplateCode());
        }
    }
}
