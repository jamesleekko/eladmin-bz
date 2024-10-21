package me.zhengjie.modules.dayufeng.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TokenConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // 可以在这里对RestTemplate进行配置
        return builder.build();
    }
}
