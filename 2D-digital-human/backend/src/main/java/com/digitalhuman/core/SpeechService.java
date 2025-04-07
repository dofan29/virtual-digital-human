package com.digitalhuman.core;

import com.microsoft.cognitiveservices.speech.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;

@Slf4j
@Service
public class SpeechService {
    
    @Value("${azure.speech.key}")
    private String subscriptionKey;
    
    @Value("${azure.speech.region}")
    private String region;
    
    public File synthesizeSpeech(String text, String outputPath) {
        try {
            SpeechConfig speechConfig = SpeechConfig.fromSubscription(subscriptionKey, region);
            speechConfig.setSpeechSynthesisVoiceName("zh-CN-XiaoxiaoNeural");
            
            try (SpeechSynthesizer synthesizer = new SpeechSynthesizer(speechConfig)) {
                SpeechSynthesisResult result = synthesizer.SpeakTextAsync(text).get();
                
                if (result.getReason() == ResultReason.SynthesizingAudioCompleted) {
                    File audioFile = new File(outputPath);
                    try (FileOutputStream stream = new FileOutputStream(audioFile)) {
                        stream.write(result.getAudioData());
                    }
                    return audioFile;
                } else {
                    log.error("语音合成失败: {}", result.getReason());
                    return null;
                }
            }
        } catch (Exception e) {
            log.error("语音合成服务出错", e);
            return null;
        }
    }
    
    public void setVoice(String voiceName) {
        // This method is no longer used in the new synthesizeSpeech method
    }
} 