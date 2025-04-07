@echo off
chcp 65001

REM 设置Java环境变量
set JAVA_HOME=D:\Program Files\jre17
set PATH=%JAVA_HOME%\bin;%PATH%

REM 设置JavaFX环境变量
set JAVAFX_HOME=D:\javafxsdk17014
set PATH=%JAVAFX_HOME%\bin;%PATH%

REM 设置Java选项
set JAVA_OPTS=--module-path "%JAVAFX_HOME%\lib" --add-modules javafx.controls,javafx.fxml,javafx.media

REM 创建必要的目录
if not exist "..\python\tts\temp" mkdir "..\python\tts\temp"
if not exist "..\python\nlp\dialogpt\temp" mkdir "..\python\nlp\dialogpt\temp"

REM 启动Python NLP服务
start cmd /k "cd ..\python\nlp\dialogpt && python -m uvicorn api:app --host 0.0.0.0 --port 8000"

REM 等待NLP服务启动
timeout /t 5

REM 启动Python TTS服务
start cmd /k "cd ..\python\tts && python -m uvicorn azure_tts:app --host 0.0.0.0 --port 8001"

REM 等待TTS服务启动
timeout /t 5

REM 启动后端服务
start cmd /k "cd ..\backend && mvn spring-boot:run"

REM 等待后端服务启动
timeout /t 10

REM 启动前端应用
start cmd /k "cd ..\frontend && mvn javafx:run"

echo 所有服务已启动！
pause 