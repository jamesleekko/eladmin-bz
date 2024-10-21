package me.zhengjie.modules.ez.rest;

import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.zhengjie.modules.ez.config.bean.TokenData;
import me.zhengjie.modules.ez.config.bean.TokenResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class EZClient {

    private static final String EZ_TOKEN_URL = "https://open.ys7.com/api/lapp/token/get";
    private static final String APP_KEY = "1c66441dc45f4e939689a2ba4b901bcd";
    private static final String APP_SECRET = "146aa934f75321813042020a9237ee86";

    public Map<String, Object> getEZToken() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 构建请求参数
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("appKey", APP_KEY);
        params.add("appSecret", APP_SECRET);

        // 构建请求体
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        // 发送请求
        ResponseEntity<String> response = restTemplate.exchange(EZ_TOKEN_URL, HttpMethod.POST, request, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            // 打印返回体
            log.info("got token {}", response.getBody());

            ObjectMapper objectMapper = new ObjectMapper();
            TokenResponse tokenResponse = objectMapper.readValue(response.getBody(), TokenResponse.class);

            String accessToken = tokenResponse.getData().getAccessToken();
            Long expireTime = tokenResponse.getData().getExpireTime();

            Map<String, Object> result = new HashMap<>();
            result.put("accessToken", accessToken);
            result.put("expireTime", expireTime);

            return result;
        } else {
            throw new Exception("Token 获取失败: " + response.getBody());
        }
    }
}