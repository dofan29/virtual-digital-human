package com.digitalhuman.model;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class Character {
    private String id;
    private String name;
    private String avatarPath;
    private String voiceId;
    private String personality;
    private String language;
    
    // 表情和动作相关属性
    private String currentEmotion;
    private String currentAction;
    
    // 对话相关属性
    private String lastResponse;
    private String conversationContext;
} 