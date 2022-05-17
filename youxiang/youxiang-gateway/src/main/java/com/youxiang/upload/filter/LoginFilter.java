package com.youxiang.upload.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.youxiang.auth.utils.JwtUtils;
import com.youxiang.common.utils.CookieUtils;
import com.youxiang.upload.config.FilterProperties;
import com.youxiang.upload.config.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

@Controller
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class LoginFilter extends ZuulFilter {
    @Autowired
    private JwtProperties jwtProp;
    @Autowired
    private FilterProperties filterProp;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        // 获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        // 获取request
        HttpServletRequest request = ctx.getRequest();
        // 获取路径
        String reqURL = request.getRequestURI();
        // 判断白名单
        // 遍历允许访问的路径
        for (String path : this.filterProp.getAllowPaths()){
            // 然后判断是否符合白名单
            if (reqURL.startsWith(path)){
                return false;
            }
        }
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        // 获取上下文
        RequestContext context = RequestContext.getCurrentContext();
        // 获取request对象
        HttpServletRequest request = context.getRequest();
        // 获取token
        String token = CookieUtils.getCookieValue(request, this.jwtProp.getCookieName());
        try {
            // 效验通过，放行
            JwtUtils.getInfoFromToken(token,this.jwtProp.getPublicKey());
        } catch (Exception e) {
            // 效验出现异常，返回403
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
        }
        return null;
    }
}
