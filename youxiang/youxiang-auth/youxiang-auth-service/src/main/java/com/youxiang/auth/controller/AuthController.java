package com.youxiang.auth.controller;

import com.youxiang.auth.config.JwtProperties;
import com.youxiang.auth.pojo.UserInfo;
import com.youxiang.auth.service.AuthService;
import com.youxiang.auth.utils.JwtUtils;
import com.youxiang.common.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProperties prop;

    /**
     * 登录授权
     * @param username
     * @param password
     * @return
     */
    @PostMapping("accredit")
    public ResponseEntity<Void> authentication(@RequestParam("username")String username,
                                               @RequestParam("password")String password,
                                               HttpServletRequest request, HttpServletResponse response){
        // 调用service方法生成token
        String token = this.authService.authentication(username,password);
        if (StringUtils.isBlank(token)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // 将token写入cookie,并指定httpOnly为true，防止通过JS获取和修改
        CookieUtils.setCookie(request,response,prop.getCookieName(),token,prop.getExpire() * 60,null,true);
        return ResponseEntity.ok().build();
    }

    /**
     * 验证用户信息
     * @param token
     * @return
     */
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verifyUser(@CookieValue("YX_TOKEN")String token,
                                               HttpServletRequest request,
                                               HttpServletResponse response){
        try {
            // 解析token获取用户信息
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, prop.getPublicKey());

            // 刷新jwt过期时间（重新生成jwt）
            token = JwtUtils.generateToken(userInfo, this.prop.getPrivateKey(), this.prop.getExpire());

            // 刷新token过期时间
            CookieUtils.setCookie(request,response,this.prop.getCookieName(),token,this.prop.getExpire() * 60,null,true);

            // 解析成功，返回用户信息
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 出现异常
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
