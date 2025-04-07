#!/bin/bash

# 启动Python NLP服务
cd ../python/nlp/dialogpt
uvicorn api:app --host 0.0.0.0 --port 8000 &

# 启动Python TTS服务
cd ../../tts
uvicorn azure_tts:app --host 0.0.0.0 --port 8001 &

# 启动Spring Boot后端
cd ../../backend
mvn spring-boot:run &

# 启动JavaFX前端
cd ../frontend
mvn javafx:run 