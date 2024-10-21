package me.zhengjie.modules.dayufeng.security;

import me.zhengjie.modules.dayufeng.sevice.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 检查当前token是否有效
        if (!tokenService.isTokenValid()) {
            // 如果token无效，则更新token
            tokenService.refreshToken();
        }
        return true;
    }
}