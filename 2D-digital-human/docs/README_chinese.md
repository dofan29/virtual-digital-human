# 2D虚拟数字人项目

这是一个基于Java和Python的2D虚拟数字人项目，集成了自然语言处理和语音合成功能。

## 技术栈

- 后端：Spring Boot
- 前端：JavaFX
- NLP：DialoGPT-small
- 语音合成：Azure Speech Services

## 系统要求

- Java 11+
- Python 3.8+
- Maven 3.6+
- CUDA支持（可选，用于GPU加速）

## 安装步骤

1. 克隆项目
```bash
git clone [项目地址]
cd 2D-digital-human
```

2. 安装Python依赖
```bash
cd python
pip install -r ../docs/requirements.txt
```

3. 配置环境变量
```bash
# Azure语音服务配置
export AZURE_SPEECH_KEY="你的Azure语音服务密钥"
export AZURE_SPEECH_REGION="你的Azure区域"

# DialoGPT模型路径
export DIALOGPT_MODEL_PATH="D:/DialoGPTmaster"
```

4. 编译Java项目
```bash
mvn clean install
```

## 运行项目

使用启动脚本运行所有服务：
```bash
cd scripts
./run.sh
```

## 项目结构

```
2D-digital-human/
├── backend/                 # Spring Boot服务
├── frontend/               # JavaFX客户端
├── python/                 # AI服务
│   ├── nlp/               # 对话模型
│   └── tts/               # 语音合成
├── docs/                  # 文档
└── scripts/               # 部署脚本
```

## 功能特性

- 实时对话生成
- 自然语音合成
- 2D角色渲染
- 表情和动作控制

## 注意事项

1. 确保DialoGPT模型已正确下载并放置在指定目录
2. 需要有效的Azure语音服务订阅
3. 首次运行可能需要下载模型文件

## 许可证

MIT License 