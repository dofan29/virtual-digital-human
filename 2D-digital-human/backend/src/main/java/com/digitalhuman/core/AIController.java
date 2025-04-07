package com.digitalhuman.core;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class AIController {
    
    @Value("${python.nlp.grpc-port}")
    private int grpcPort;
    
    private ManagedChannel channel;
    private final RestTemplate restTemplate = new RestTemplate();
    private String currentContext = "";
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY = 1000; // 1秒
    
    @PostConstruct
    public void init() {
        channel = ManagedChannelBuilder.forAddress("localhost", grpcPort)
                .usePlaintext()
                .build();
        log.info("gRPC channel initialized on port: {}", grpcPort);
    }
    
    @PreDestroy
    public void shutdown() throws InterruptedException {
        if (channel != null) {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
    
    @Retryable(value = {ResourceAccessException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public String generateResponse(String input, String context) {
        log.info("开始生成回复，输入: {}, 上下文: {}", input, context);
        
        int retryCount = 0;
        while (retryCount < MAX_RETRIES) {
            try {
                // 使用HTTP请求与Python DialoGPT模型通信
                String url = "http://localhost:8000/chat";
                log.info("尝试连接NLP服务: {}", url);
                
                // 准备请求体
                Map<String, String> requestBody = new HashMap<>();
                requestBody.put("message", input);
                requestBody.put("context", context != null ? context : currentContext);
                
                // 设置请求头
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                
                // 创建请求实体
                HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
                
                // 发送请求
                log.info("发送请求到NLP服务");
                @SuppressWarnings("unchecked")
                Map<String, String> response = restTemplate.postForObject(url, requestEntity, Map.class);
                
                if (response != null && response.containsKey("response")) {
                    String result = response.get("response");
                    log.info("成功获取回复: {}", result);
                    return result;
                } else {
                    log.error("NLP服务返回无效响应");
                    return "抱歉，我现在无法回答。";
                }
            } catch (ResourceAccessException e) {
                retryCount++;
                log.error("第{}次尝试连接NLP服务失败", retryCount, e);
                
                if (retryCount < MAX_RETRIES) {
                    log.info("等待{}毫秒后重试", RETRY_DELAY);
                    try {
                        Thread.sleep(RETRY_DELAY);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        log.error("重试等待被中断", ie);
                        return "抱歉，服务暂时不可用。";
                    }
                } else {
                    log.error("已达到最大重试次数，NLP服务不可用");
                    return "抱歉，我遇到了一些问题，无法正常回答。";
                }
            } catch (Exception e) {
                log.error("与NLP服务通信时发生错误", e);
                return "抱歉，我遇到了一些问题，无法正常回答。";
            }
        }
        
        return "抱歉，服务暂时不可用。";
    }
    
    public void updateContext(String newContext) {
        if (newContext != null && !newContext.isEmpty()) {
            this.currentContext = newContext;
            log.info("对话上下文已更新: {}", newContext);
        } else {
            log.warn("尝试使用空值更新上下文");
        }
    }
} 