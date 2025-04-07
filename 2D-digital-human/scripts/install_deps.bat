@echo off
chcp 65001
echo 正在使用国内镜像安装Python依赖...

REM 创建pip配置目录
if not exist "%APPDATA%\pip" mkdir "%APPDATA%\pip"

REM 复制pip配置文件
copy /Y "..\python\pip.ini" "%APPDATA%\pip\pip.ini"

REM 安装依赖
cd ..\python

REM 设置pip超时时间和重试次数
set PIP_DEFAULT_TIMEOUT=100
set PIP_RETRIES=3

REM 更新pip到最新版本
echo 正在更新pip...
python -m pip install --upgrade pip -i https://pypi.tuna.tsinghua.edu.cn/simple --trusted-host pypi.tuna.tsinghua.edu.cn

echo 正在安装基本依赖...
pip install --timeout 100 --retries 3 fastapi==0.68.1 uvicorn==0.15.0 pydantic==1.8.2 python-multipart==0.0.5 -i https://pypi.tuna.tsinghua.edu.cn/simple --trusted-host pypi.tuna.tsinghua.edu.cn
if errorlevel 1 (
    echo 基本依赖安装失败，尝试使用备用镜像源...
    pip install --timeout 100 --retries 3 fastapi==0.68.1 uvicorn==0.15.0 pydantic==1.8.2 python-multipart==0.0.5 -i https://mirrors.aliyun.com/pypi/simple/ --trusted-host mirrors.aliyun.com
)

echo 正在安装PyTorch...
pip install --timeout 100 --retries 3 torch==2.0.0 -i https://pypi.tuna.tsinghua.edu.cn/simple --trusted-host pypi.tuna.tsinghua.edu.cn
if errorlevel 1 (
    echo PyTorch安装失败，尝试使用备用镜像源...
    pip install --timeout 100 --retries 3 torch==2.0.0 -i https://mirrors.aliyun.com/pypi/simple/ --trusted-host mirrors.aliyun.com
)

echo 正在安装tokenizers...
pip install --timeout 100 --retries 3 --only-binary :all: tokenizers==0.15.2 -i https://pypi.tuna.tsinghua.edu.cn/simple --trusted-host pypi.tuna.tsinghua.edu.cn
if errorlevel 1 (
    echo tokenizers安装失败，尝试使用备用镜像源...
    pip install --timeout 100 --retries 3 --only-binary :all: tokenizers==0.15.2 -i https://mirrors.aliyun.com/pypi/simple/ --trusted-host mirrors.aliyun.com
)

echo 正在安装transformers...
pip install --timeout 100 --retries 3 transformers==4.11.3 --no-deps -i https://pypi.tuna.tsinghua.edu.cn/simple --trusted-host pypi.tuna.tsinghua.edu.cn
if errorlevel 1 (
    echo transformers安装失败，尝试使用备用镜像源...
    pip install --timeout 100 --retries 3 transformers==4.11.3 --no-deps -i https://mirrors.aliyun.com/pypi/simple/ --trusted-host mirrors.aliyun.com
)

echo 正在安装azure-cognitiveservices-speech...
pip install --timeout 100 --retries 3 azure-cognitiveservices-speech==1.43.0 -i https://pypi.tuna.tsinghua.edu.cn/simple --trusted-host pypi.tuna.tsinghua.edu.cn
if errorlevel 1 (
    echo azure-cognitiveservices-speech安装失败，尝试使用备用镜像源...
    pip install --timeout 100 --retries 3 azure-cognitiveservices-speech==1.43.0 -i https://mirrors.aliyun.com/pypi/simple/ --trusted-host mirrors.aliyun.com
)

echo Python依赖安装完成！
pause 