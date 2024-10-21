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
import org.springframework.stereotype.Service;

//import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class TokenService {

    @Getter
    @Setter
    private String token;

    @Getter
    @Setter
    private Long expiresIn;

    @Value("${dayufeng.username}")
    private String username;

    @Value("${dayufeng.password}")
    private String password;

    @Value("${dayufeng.login-type}")
    private String loginType;

    @Autowired
    @Lazy  // 延迟加载DayufengApiClient，避免循环依赖
    private DayufengApiClient apiClient;

//    @PostConstruct
//    public void init() {
//        refreshToken();
//    }

    // 检查当前Token是否有效
    public boolean isTokenValid() {
        // 根据需要实现token的验证逻辑，例如根据过期时间判断
        return token != null && !isTokenExpired();
    }

    // 刷新token
    public void refreshToken() {
        log.info("Refreshing token...");
        String loginUrl = "https://api.dayufeng.cn/get/token";
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", username);
        credentials.put("password", password);
        credentials.put("login_type", loginType);

        ResponseEntity<TokenResponse> response = apiClient.sendRequest(loginUrl, HttpMethod.POST, credentials, TokenResponse.class, false, false);
        if (response.getStatusCode() == HttpStatus.OK) {
            TokenResponse responseBody = response.getBody();
            log.info("get token {}", responseBody);
            if (responseBody != null) {
//                token = responseBody.get("token");
                // 更新token的逻辑，例如存储到数据库或缓存
            }
        } else {
            throw new RuntimeException("Failed to refresh token");
        }
    }

    private boolean isTokenExpired() {
        if (expiresIn == null) {
            return true;
        } else {
            return System.currentTimeMillis() > expiresIn;
        }
    }
}