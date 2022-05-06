package com.youxiang.user.service;

import com.youxiang.user.pojo.User;

public interface UserService {
    Boolean checkUser(String data, Integer type);

    Boolean sendVerifyCode(String phone);

    Boolean registry(User user, String code);

    User queryUser(String username, String password);
}
