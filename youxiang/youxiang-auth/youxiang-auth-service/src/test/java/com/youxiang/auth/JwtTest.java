package com.youxiang.auth;

import com.youxiang.auth.pojo.UserInfo;
import com.youxiang.auth.utils.JwtUtils;
import com.youxiang.auth.utils.RsaUtils;
import org.junit.Test;
import org.junit.Before;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtTest {

    private static final String pubKeyPath = "C:\\java\\rsa\\rsa.pub";

    private static final String priKeyPath = "C:\\java\\rsa\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
    }

    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTY1MTkzMzgxOH0.QVcVrlb-PiKyEuxq-qc6UGV_MfPfLGWUh7Y0XA-QdnOx8clm2-jTY0vn_W3gLpIwe_T6bd_o7Vt4dMZxygWanpsCwgytVhY4cC7ae563YScBeXXpZut0F3ozMsTBAJrrkvop0kH0T2G7vb-0wVWNRvh6MLnwH8xjLRZLhHmR9E4";
        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}
