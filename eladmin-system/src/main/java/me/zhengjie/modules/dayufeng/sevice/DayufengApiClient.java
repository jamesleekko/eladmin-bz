package me.zhengjie.modules.dayufeng.sevice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class DayufengApiClient {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    @Lazy  // 延迟加载TokenService，避免循环依赖
    private DayufengTokenService dayufengTokenService;

    public <T> ResponseEntity<T> sendRequest(String url, HttpMethod method, Object requestBody, Class<T> responseType, boolean needAuth, boolean retryAllowed) {
        return sendRequestWithRetry(url, method, requestBody, responseType, needAuth, retryAllowed);
    }

    // 处理带重试的请求，retryAllowed 控制是否允许重试
    private <T> ResponseEntity<T> sendRequestWithRetry(String url, HttpMethod method, Object requestBody, Class<T> responseType, boolean needAuth, boolean retryAllowed) {
        HttpHeaders headers = new HttpHeaders();
        if (needAuth) {
            headers.set("Authorization", "Bearer " + dayufengTokenService.getToken());
        }
        HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<T> response = restTemplate.exchange(url, method, requestEntity, responseType);

            // 正常返回，直接返回响应
            return response;
        } catch (HttpClientErrorException e) {
            // 捕获401 Unauthorized错误
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED && retryAllowed) {
                log.warn("Token expired, attempting to refresh token and retry request...");

                // 重新获取新的token
                dayufengTokenService.refreshToken();

                // 重发请求，retryAllowed设为false避免无限循环
                return sendRequestWithRetry(url, method, requestBody, responseType, true, false);
            } else {
                log.error("Request failed: {}", e.getMessage());
                throw e;  // 抛出其他错误
            }
        }
    }
}
