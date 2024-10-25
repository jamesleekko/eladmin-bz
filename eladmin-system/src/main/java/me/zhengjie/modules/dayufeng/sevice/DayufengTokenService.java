package me.zhengjie.modules.dayufeng.sevice;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.dayufeng.data.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class DayufengTokenService {

    @Getter
    @Setter
    private String token;

    @Getter
    @Setter
    private Long expiresTimeSecond;

    @Value("${dayufeng.base-url}")
    private String baseUrl;

    @Value("${dayufeng.username}")
    private String username;

    @Value("${dayufeng.password}")
    private String password;

    @Value("${dayufeng.login-type}")
    private String loginType;

    @Autowired
    @Lazy  // 延迟加载DayufengApiClient，避免循环依赖
    private DayufengApiClient apiClient;

    @PostConstruct
    public void init() {
        refreshToken();
    }

    @Scheduled(fixedRate = 1000 * 60 * 60 * 24) //每天刷新一次
    public void refreshIfNeeded() {
        if (System.currentTimeMillis() / 1000 > expiresTimeSecond) {
            log.info("Token即将过期，重新获取...");
            refreshToken();
        }
    }

    // 检查当前Token是否有效
    public boolean isTokenValid() {
        return token != null && !isTokenExpired();
    }

    // 刷新token
    public void refreshToken() {
        log.info("Refreshing token...");
        String loginUrl = baseUrl + "/get/token";
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", username);
        credentials.put("password", password);
        credentials.put("login_type", loginType);

        ResponseEntity<TokenResponse> response = apiClient.sendRequest(loginUrl, HttpMethod.POST, credentials, TokenResponse.class, false, false);
        if (response.getStatusCode() == HttpStatus.OK) {
            TokenResponse responseBody = response.getBody();
            log.info("get dayufeng token {}", responseBody);
            if (responseBody != null) {
                // 更新token的逻辑，例如存储到数据库或缓存
                setToken(responseBody.getData().getAccess_token());
                setExpiresTimeSecond(System.currentTimeMillis() / 1000 + responseBody.getData().getExpires_in());
            }
        } else {
            throw new RuntimeException("Failed to refresh token");
        }
    }

    private boolean isTokenExpired() {
        if (expiresTimeSecond == null) {
            return true;
        } else {
            return System.currentTimeMillis() / 1000 > expiresTimeSecond;
        }
    }
}