@echo off
chcp 65001

REM 设置Python环境变量
set PYTHONPATH=%PYTHONPATH%;%~dp0..\python

REM 设置DialoGPT模型路径
set DIALOGPT_MODEL_PATH=D:\DialoGPTmaster

REM 启动NLP服务
cd ..\python\nlp\dialogpt
python -m uvicorn api:app --host 0.0.0.0 --port 8000

echo NLP服务已启动！ 