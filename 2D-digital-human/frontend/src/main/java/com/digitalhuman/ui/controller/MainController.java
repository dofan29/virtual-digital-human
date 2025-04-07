package com.digitalhuman.ui.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.application.Platform;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MainController {
    
    @FXML
    private ImageView characterView;
    
    @FXML
    private TextArea chatArea;
    
    @FXML
    private TextField inputField;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private MediaPlayer mediaPlayer;
    private Random random = new Random();
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    
    // 表情和动作图片路径
    private String[] emotions = {
        "/images/emotions/happy.png",
        "/images/emotions/sad.png",
        "/images/emotions/angry.png",
        "/images/emotions/surprised.png"
    };
    
    private String[] actions = {
        "/images/actions/wave.png",
        "/images/actions/nod.png",
        "/images/actions/think.png",
        "/images/actions/smile.png"
    };
    
    @FXML
    public void initialize() {
        // 初始化角色图片
        characterView.setImage(new Image(
            getClass().getResourceAsStream("/images/character.png")
        ));
        
        // 添加初始动画效果
        addInitialAnimation();
        
        // 显示欢迎消息
        appendMessage("数字人", "你好！我是你的2D虚拟助手，有什么我可以帮你的吗？");
    }
    
    private void addInitialAnimation() {
        // 角色淡入动画
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), characterView);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
        
        // 角色轻微缩放动画
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(1500), characterView);
        scaleTransition.setFromX(0.9);
        scaleTransition.setFromY(0.9);
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
        scaleTransition.play();
    }
    
    @FXML
    private void handleSendMessage() {
        String message = inputField.getText();
        if (message.isEmpty()) return;
        
        // 显示用户消息
        appendMessage("你", message);
        inputField.clear();
        
        // 发送消息到后端
        Map<String, String> request = new HashMap<>();
        request.put("message", message);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);
        
        // 在新线程中处理API请求
        new Thread(() -> {
            try {
                // 添加思考动画
                Platform.runLater(() -> {
                    addThinkingAnimation();
                });
                
                ResponseEntity<String> response = restTemplate.postForEntity(
                    "http://localhost:8080/api/chat/message",
                    entity,
                    String.class
                );
                
                if (response.getBody() != null) {
                    Map<String, Object> responseMap = objectMapper.readValue(
                        response.getBody(),
                        new TypeReference<Map<String, Object>>() {}
                    );
                    
                    String reply = (String) responseMap.get("text");
                    String audioUrl = (String) responseMap.get("audioUrl");
                    
                    // 在JavaFX线程中更新UI
                    Platform.runLater(() -> {
                        // 显示回复
                        appendMessage("数字人", reply);
                        
                        // 播放语音
                        if (audioUrl != null) {
                            playAudio("http://localhost:8080" + audioUrl);
                        }
                        
                        // 随机切换表情
                        if (random.nextInt(100) < 30) {
                            handleChangeEmotion();
                        }
                    });
                }
            } catch (Exception e) {
                Platform.runLater(() -> {
                    appendMessage("系统", "错误: " + e.getMessage());
                });
            }
        }).start();
    }
    
    private void addThinkingAnimation() {
        // 轻微上下移动动画
        TranslateTransition moveTransition = new TranslateTransition(Duration.millis(500), characterView);
        moveTransition.setFromY(0);
        moveTransition.setToY(-10);
        moveTransition.setAutoReverse(true);
        moveTransition.setCycleCount(2);
        moveTransition.play();
    }
    
    private void appendMessage(String sender, String message) {
        String timestamp = LocalTime.now().format(timeFormatter);
        String formattedMessage = String.format("[%s] %s: %s\n", timestamp, sender, message);
        chatArea.appendText(formattedMessage);
        // 自动滚动到底部
        chatArea.setScrollTop(Double.MAX_VALUE);
        
        // 如果是数字人回复，添加回复动画
        if (sender.equals("数字人")) {
            addReplyAnimation();
        }
    }
    
    private void addReplyAnimation() {
        // 轻微缩放动画
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(300), characterView);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.05);
        scaleTransition.setToY(1.05);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(2);
        scaleTransition.play();
    }
    
    private void playAudio(String url) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.dispose();
            }
            
            Media media = new Media(url);
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        } catch (Exception e) {
            appendMessage("系统", "播放语音失败: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleChangeEmotion() {
        String emotionPath = emotions[random.nextInt(emotions.length)];
        
        // 添加淡入淡出效果
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), characterView);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            characterView.setImage(new Image(getClass().getResourceAsStream(emotionPath)));
            
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), characterView);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
    }
    
    @FXML
    private void handleChangeAction() {
        String actionPath = actions[random.nextInt(actions.length)];
        
        // 添加淡入淡出效果
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), characterView);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            characterView.setImage(new Image(getClass().getResourceAsStream(actionPath)));
            
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), characterView);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
    }
} 