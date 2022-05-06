package com.youxiang.user.service.impl;

import com.youxiang.common.utils.NumberUtils;
import com.youxiang.user.mapper.UserMapper;
import com.youxiang.user.pojo.User;
import com.youxiang.user.service.UserService;
import com.youxiang.utils.CodecUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final static String KEY_PREFIX = "user:code:phone:";

    static final Logger logger = LoggerFactory.getLogger(UserService.class);

    /**
     * 校验用户名或者手机号是否可用
     * @param data
     * @param type
     * @return
     */
    @Override
    public Boolean checkUser(String data, Integer type) {
        User user = new User();
        if (type == 1){
            user.setUsername(data);
        } else if (type == 2){
            user.setPhone(data);
        }else {
            return null;
        }
        return this.userMapper.selectCount(user) == 0;
    }

    /**
     * 发送手机验证码
     * @param phone
     * @return
     */
    @Override
    public Boolean sendVerifyCode(String phone) {
        // 判断手机号是否为空
        if (StringUtils.isBlank(phone)){
            return false;
        }
        // 生成验证码
        String code = NumberUtils.generateCode(6);
        try {
            // 发送信息
            Map<String, String> map = new HashMap<>();
            map.put("phone",phone);
            map.put("code",code);
            this.amqpTemplate.convertAndSend("YOUXIANG.SMS.EXCHANGE","sms.verify",map);
            // 存储到redis，5分钟后失效
            this.redisTemplate.opsForValue().set(KEY_PREFIX + phone,code,5, TimeUnit.MINUTES);
            return true;
        }catch (Exception e){
            logger.error("发送短信失败：phone：{}，code：{}",phone,code);
            return false;
        }
    }

    /**
     * 用户注册
     * @param user
     * @param code
     */
    @Override
    public Boolean registry(User user, String code) {
        // 1.效验验证码
        String redisCode = this.redisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        if (!StringUtils.equals(code,redisCode)){
            return false;
        }
        // 2.生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        // 3.加盐加密
        String md5Hex = CodecUtils.md5Hex(user.getPassword(), salt);
        user.setPassword(md5Hex);
        // 4.新增用户
        user.setId(null);
        user.setCreated(new Date());
        Boolean bool = this.userMapper.insertSelective(user) == 1;
        // 5.删除redis中的验证码
        if (bool){
            this.redisTemplate.delete(KEY_PREFIX + user.getPhone());
        }
        return bool;
    }

    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    @Override
    public User queryUser(String username, String password) {
        // 1.先根据用户名查询用户
        User record = new User();
        record.setUsername(username);
        User user = this.userMapper.selectOne(record);
        // 2.判断用户是否存在，如不存在则无需进行下一步
        if (user == null){
            return null;
        }
        // 3.对用户输入的密码进行加盐加密
        password = CodecUtils.md5Hex(password, user.getSalt());
        // 4.判断用户输入的密码和查询出的密码是否一致
        if (!StringUtils.equals(password,user.getPassword())){
            return null;
        }
        return user;
    }
}
