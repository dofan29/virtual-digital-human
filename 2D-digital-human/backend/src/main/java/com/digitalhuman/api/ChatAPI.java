package com.digitalhuman.api;

import com.digitalhuman.core.AIController;
import com.digitalhuman.core.SpeechService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/chat")
public class ChatAPI {
    
    private final AIController aiController;
    private final SpeechService speechService;
    
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;
    
    public ChatAPI(AIController aiController, SpeechService speechService) {
        this.aiController = aiController;
        this.speechService = speechService;
    }
    
    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendMessage(@RequestBody Map<String, String> request) {
        try {
            String message = request.get("message");
            log.info("收到消息: {}", message);
            
            // 调用Python NLP服务
            String nlpResponse = restTemplate.postForObject(
                "http://localhost:5000/chat",
                Map.of("message", message),
                String.class
            );

            // 解析Python服务的响应
            Map<String, Object> response = objectMapper.readValue(nlpResponse, new TypeReference<Map<String, Object>>() {});
            log.info("NLP服务响应: {}", response);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("处理消息时发生错误", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "处理消息时发生错误: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @PostMapping(value = "/message", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> processMessage(@RequestBody Map<String, String> request) {
        try {
            String userMessage = request.get("message");
            log.info("收到用户消息: {}", userMessage);
            
            // 生成回复
            String response = aiController.generateResponse(userMessage, "");
            log.info("生成回复: {}", response);
            
            // 生成语音文件
            String audioPath = "temp/" + System.currentTimeMillis() + ".wav";
            log.info("开始生成语音文件: {}", audioPath);
            
            // 确保temp目录存在
            File tempDir = new File("temp");
            if (!tempDir.exists()) {
                log.info("创建temp目录");
                tempDir.mkdirs();
            }
            
            File audioFile = speechService.synthesizeSpeech(response, audioPath);
            if (audioFile != null) {
                log.info("语音文件生成成功: {}", audioFile.getAbsolutePath());
            } else {
                log.error("语音文件生成失败");
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("text", response);
            result.put("audioUrl", audioFile != null ? "/audio/" + audioFile.getName() : null);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("处理消息时发生错误", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "处理消息时发生错误: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    @PostMapping(value = "/context", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateContext(@RequestBody Map<String, String> request) {
        try {
            String newContext = request.get("context");
            log.info("更新上下文: {}", newContext);
            aiController.updateContext(newContext);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("更新上下文时发生错误", e);
            return ResponseEntity.badRequest().build();
        }
    }
} 