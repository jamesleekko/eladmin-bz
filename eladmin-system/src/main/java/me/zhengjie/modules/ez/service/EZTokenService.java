package me.zhengjie.modules.ez.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.ez.rest.EZClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@Slf4j
@Service
public class EZTokenService implements InitializingBean {

    private final EZDeviceService ezDeviceService;

    @Getter
    private String accessToken;
    private Long expireTime;

    @Autowired
    public EZTokenService(EZDeviceService ezDeviceService) {
        this.ezDeviceService = ezDeviceService;
    }

    @Override
    public void afterPropertiesSet() {
        fetchToken();
        ezDeviceService.fetchAndSaveDeviceList(accessToken);
    }

    public void fetchToken() {
        // 调用获取 token 的 API
        EZClient ezClient = new EZClient();
        try {
            // 获取 token
            Map<String, Object> result = ezClient.getEZToken();
            accessToken = result.get("accessToken").toString();
            expireTime = Long.valueOf(result.get("expireTime").toString());
            log.info("已保存萤石token: " + accessToken);
        } catch (Exception e) {
            System.err.println("获取 token 失败: " + e.getMessage());
        }
    }

    public boolean isTokenExpired() {
        LocalDateTime expireDateTime = Instant.ofEpochMilli(expireTime)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        return LocalDateTime.now().isAfter(expireDateTime);
    }

}