package me.zhengjie.modules.ez.config.interceptor;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        // 打印请求url
        System.out.println("=== Request URL ===");
        System.out.println(request.getURI());

        // 打印请求头
        System.out.println("=== Request Headers ===");
        request.getHeaders().forEach((key, value) -> System.out.println(key + ": " + value.stream().collect(Collectors.joining(","))));

        // 打印请求体
        System.out.println("=== Request Body ===");
        System.out.println(new String(body, StandardCharsets.UTF_8));

        // 执行请求并返回响应
        ClientHttpResponse response = execution.execute(request, body);

        return response;
    }
}