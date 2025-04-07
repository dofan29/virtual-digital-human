@echo off
chcp 65001

REM 设置Java环境变量
set JAVA_HOME=D:\Program Files\jre17
set PATH=%JAVA_HOME%\bin;%PATH%

REM 设置Python环境变量
setx PYTHON_HOME "D:\Program Files\Python311"
setx PATH "%PATH%;%PYTHON_HOME%;%PYTHON_HOME%\Scripts"

REM 设置Maven环境变量
setx MAVEN_HOME "D:\Program Files\maven399"
setx PATH "%PATH%;%MAVEN_HOME%\bin"

REM 设置JavaFX环境变量
set JAVAFX_HOME=D:\javafxsdk17014
set PATH=%JAVAFX_HOME%\bin;%PATH%

REM 设置Azure语音服务环境变量
set AZURE_SPEECH_KEY="your-azure-speech-service-key"
set AZURE_SPEECH_REGION="your-azure-region"

REM 设置DialoGPT模型路径
setx DIALOGPT_MODEL_PATH "D:\DialoGPTmaster"

echo 环境变量设置完成！
pause 