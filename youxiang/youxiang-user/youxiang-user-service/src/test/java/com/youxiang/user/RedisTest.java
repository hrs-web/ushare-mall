package com.youxiang.user;

import com.youxiang.YouxiangUserApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = YouxiangUserApplication.class)
public class RedisTest {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void redisTest(){
        // 储存数据
        this.redisTemplate.opsForValue().set("key11","value11");
        // 获取数据
        String val = redisTemplate.opsForValue().get("key11");
        System.out.println("vla = "+val);
    }

    @Test
    public void redisTest2(){
        // 储存数据，设置5秒后过期
        this.redisTemplate.opsForValue().set("key2","value2",60, TimeUnit.SECONDS);
    }

    @Test
    public void redisTest3(){
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps("user");
        // 操作hash数据
        hashOps.put("name","jack");
        hashOps.put("age","21");
        // 获取单个数据
        Object name = hashOps.get("name");
        System.out.println("name = "+name);
        // 获取所有数据，遍历
        Map<Object, Object> entries = hashOps.entries();
        for (Map.Entry<Object, Object> me : entries.entrySet()){
            System.out.println(me.getKey() + " : " + me.getValue());
        }

    }
}
