# 2D虚拟数字人安装指南

本文档提供了详细的安装和配置步骤，帮助您成功运行2D虚拟数字人项目。

## 环境准备

### 1. Java环境

项目使用Java 17。您已经安装了Java 17 JRE，路径为：`D:\Program Files\jre17`

### 2. Python环境

项目使用Python 3.8或更高版本。您已经安装了Python 3.11，路径为：`D:\Program Files\Python311`

### 3. Maven环境

项目使用Maven 3.6或更高版本。您已经安装了Maven 3.9.9，路径为：`D:\Program Files\maven399`

### 4. JavaFX环境

项目使用JavaFX 17.0.1。您已经安装了JavaFX SDK，路径为：`D:\javafxsdk17014`

## 安装步骤

### 1. 设置环境变量

运行环境变量设置脚本：

```bash
cd scripts
.\set_env.bat
```

这将设置以下环境变量：
- JAVA_HOME
- PYTHON_HOME
- MAVEN_HOME
- JAVAFX_HOME
- AZURE_SPEECH_KEY
- AZURE_SPEECH_REGION
- DIALOGPT_MODEL_PATH

### 2. 安装Python依赖

使用国内镜像安装Python依赖（推荐）：

```bash
cd scripts
.\install_deps.bat
```

或者手动安装：

```bash
cd python
pip install fastapi==0.68.1 uvicorn==0.15.0 pydantic==1.8.2 python-multipart==0.0.5 -i https://pypi.tuna.tsinghua.edu.cn/simple --trusted-host pypi.tuna.tsinghua.edu.cn
pip install torch==2.0.0 -i https://pypi.tuna.tsinghua.edu.cn/simple --trusted-host pypi.tuna.tsinghua.edu.cn
pip install transformers==4.11.3 azure-cognitiveservices-speech==1.43.0 -i https://pypi.tuna.tsinghua.edu.cn/simple --trusted-host pypi.tuna.tsinghua.edu.cn
```

> **注意**：PyTorch安装可能需要一些时间，请耐心等待。如果安装失败，可以尝试从PyTorch官网下载对应的wheel文件手动安装。

### 3. 准备角色图片

在`frontend/src/main/resources/images`目录下放置以下图片：

- `character.png` - 默认角色图片
- `emotions/happy.png` - 开心表情
- `emotions/sad.png` - 伤心表情
- `emotions/angry.png` - 生气表情
- `emotions/surprised.png` - 惊讶表情
- `actions/wave.png` - 挥手动作
- `actions/nod.png` - 点头动作
- `actions/think.png` - 思考动作
- `actions/smile.png` - 微笑动作

### 4. 编译Java项目

```bash
cd backend
mvn clean install

cd ../frontend
mvn clean install
```

## 运行项目

使用启动脚本运行所有服务：

```bash
cd scripts
.\run.bat
```

这将启动以下服务：
1. Python NLP服务 (端口8000)
2. Python TTS服务 (端口8001)
3. Spring Boot后端 (端口8080)
4. JavaFX前端

## 故障排除

### 1. 端口冲突

如果遇到端口冲突，请修改以下文件中的端口配置：

- `backend/src/main/resources/application.yml` - 修改Spring Boot端口
- `python/nlp/dialogpt/api.py` - 修改NLP服务端口
- `python/tts/azure_tts.py` - 修改TTS服务端口

### 2. 图片加载失败

确保所有图片文件已正确放置在指定目录，并且文件名与代码中的引用一致。

### 3. Azure语音服务错误

检查Azure语音服务密钥和区域是否正确设置。您可以在`application.yml`文件中直接设置这些值：

```yaml
azure:
  speech:
    subscription-key: 7X46DA3q3hB6koqtOKqc2LGFHfwtZwocbU94XkGTqm3L5BEmiYSQJQQJ99BDAC3pKaRXJ3w3AAAYACOGzraO
    region: eastasia
```

### 4. DialoGPT模型加载失败

确保DialoGPT模型已正确下载并放置在指定目录：`D:\DialoGPTmaster`

### 5. JavaFX加载失败

如果遇到JavaFX加载问题，请检查以下内容：

- 确保JavaFX SDK路径正确：`D:\javafxsdk17014`
- 检查`JAVAFX_HOME`环境变量是否正确设置
- 确保`frontend/pom.xml`中的JavaFX配置正确

### 6. Python依赖安装失败

如果使用国内镜像安装依赖仍然失败，可以尝试以下方法：

- 手动下载依赖包并安装：
  ```bash
  pip install --no-index --find-links=path/to/download/directory -r requirements.txt
  ```
- 尝试其他国内镜像源：
  - 阿里云：https://mirrors.aliyun.com/pypi/simple/
  - 中国科技大学：https://pypi.mirrors.ustc.edu.cn/simple/
  - 豆瓣：https://pypi.douban.com/simple/
- 对于PyTorch，可以尝试从官网下载对应的wheel文件：
  ```bash
  # 下载PyTorch wheel文件
  curl -O https://download.pytorch.org/whl/cu118/torch-2.0.0%2Bcu118-cp311-cp311-win_amd64.whl
  
  # 安装下载的wheel文件
  pip install torch-2.0.0+cu118-cp311-cp311-win_amd64.whl
  ```

## 自定义配置

### 修改角色属性

在`backend/src/main/java/com/digitalhuman/model/Character.java`中修改角色属性。

### 修改对话模型参数

在`python/nlp/dialogpt/model.py`中修改DialoGPT模型的生成参数。

### 修改语音合成设置

在`backend/src/main/java/com/digitalhuman/core/SpeechService.java`中修改Azure语音合成设置。 