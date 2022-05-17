package com.youxiang.cart.intercept;

import com.youxiang.auth.pojo.UserInfo;
import com.youxiang.auth.utils.JwtUtils;
import com.youxiang.cart.config.JwtProperties;
import com.youxiang.common.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@EnableConfigurationProperties(JwtProperties.class)
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private JwtProperties jwtProperties;

    private static final ThreadLocal<UserInfo> THREAD_LOCAL = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取token
        String token = CookieUtils.getCookieValue(request, this.jwtProperties.getCookieName());

        // 判断是否为空
        if (StringUtils.isBlank(token)){
            // 未登录,返回401
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        try {
            // 解析token，获取用户信息
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, this.jwtProperties.getPublicKey());
            // 放入线程域
            THREAD_LOCAL.set(userInfo);

            return true;
        }catch (Exception e){
            // 抛出异常，证明未登录,返回401
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

    }

    public static UserInfo getLoginUser(){
        return THREAD_LOCAL.get();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清除线程变量，必须操作。
        // 因为我们使用的是线程池，一次请求执行完成之后，线程并没有结束。
        THREAD_LOCAL.remove();
    }
}
