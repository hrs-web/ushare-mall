package com.youxiang.auth.service.impl;

import com.youxiang.auth.client.UserClient;
import com.youxiang.auth.config.JwtProperties;
import com.youxiang.auth.pojo.UserInfo;
import com.youxiang.auth.service.AuthService;
import com.youxiang.auth.utils.JwtUtils;
import com.youxiang.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtProperties prop;

    @Override
    public String authentication(String username, String password) {
        // 1.远程调用查询用户接口
        User user = this.userClient.queryUser(username, password);
        // 2.判断用户是否存在
        if (user == null){
            return null;
        }
        // 生成jwt类型token
        try {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(user.getId());
            userInfo.setUsername(user.getUsername());
            String token = JwtUtils.generateToken(userInfo, prop.getPrivateKey(), prop.getExpire());
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
