package me.zhengjie.modules.dayufeng.security;

import me.zhengjie.modules.dayufeng.sevice.DayufengTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private DayufengTokenService dayufengTokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 检查当前token是否有效
        if (!dayufengTokenService.isTokenValid()) {
            // 如果token无效，则更新token
            dayufengTokenService.refreshToken();
        }
        return true;
    }
}