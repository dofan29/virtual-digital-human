import azure.cognitiveservices.speech as speechsdk
import os
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import uuid

app = FastAPI()

class TTSRequest(BaseModel):
    text: str
    voice_name: str = "zh-CN-XiaoxiaoNeural"

class TTSResponse(BaseModel):
    audio_path: str

# Azure语音服务配置
speech_key = os.getenv("AZURE_SPEECH_KEY")
service_region = os.getenv("AZURE_SPEECH_REGION")

# 创建语音配置
speech_config = speechsdk.SpeechConfig(
    subscription=speech_key, 
    region=service_region
)

@app.post("/synthesize", response_model=TTSResponse)
async def synthesize_speech(request: TTSRequest):
    try:
        # 设置语音
        speech_config.speech_synthesis_voice_name = request.voice_name
        
        # 创建语音合成器
        synthesizer = speechsdk.SpeechSynthesizer(speech_config=speech_config)
        
        # 生成音频文件路径
        audio_path = f"temp/{uuid.uuid4()}.wav"
        
        # 合成语音
        result = synthesizer.speak_text_async(request.text).get()
        
        if result.reason == speechsdk.ResultReason.SynthesizingAudioCompleted:
            # 保存音频文件
            with open(audio_path, "wb") as audio_file:
                audio_file.write(result.audio_data)
            return TTSResponse(audio_path=audio_path)
        else:
            raise HTTPException(
                status_code=500,
                detail=f"Speech synthesis failed: {result.reason}"
            )
            
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/health")
async def health_check():
    return {"status": "healthy"} 