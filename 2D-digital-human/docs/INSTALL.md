# 2D Virtual Digital Human Installation Guide

This document provides detailed installation and configuration steps to help you successfully run the 2D Virtual Digital Human project.

## Environment Preparation

### 1. Java Environment

The project uses Java 17. You have Java 17 JRE installed at: `D:\Program Files\jre17`

### 2. Python Environment

The project uses Python 3.8 or higher. You have Python 3.11 installed at: `D:\Program Files\Python311`

### 3. Maven Environment

The project uses Maven 3.6 or higher. You have Maven 3.9.9 installed at: `D:\Program Files\maven399`

### 4. JavaFX Environment

The project uses JavaFX 17.0.1. You have JavaFX SDK installed at: `D:\javafxsdk17014`

## Installation Steps

### 1. Set Environment Variables

Run the environment variable setup script:

```bash
cd scripts
.\set_env.bat
```

This will set the following environment variables:
- JAVA_HOME
- PYTHON_HOME
- MAVEN_HOME
- JAVAFX_HOME
- AZURE_SPEECH_KEY
- AZURE_SPEECH_REGION
- DIALOGPT_MODEL_PATH

### 2. Install Python Dependencies

Install Python dependencies using Chinese mirror (recommended):

```bash
cd scripts
.\install_deps.bat
```

Or install manually:

```bash
cd python
pip install fastapi==0.68.1 uvicorn==0.15.0 pydantic==1.8.2 python-multipart==0.0.5 -i https://pypi.tuna.tsinghua.edu.cn/simple --trusted-host pypi.tuna.tsinghua.edu.cn
pip install torch==2.0.0 -i https://pypi.tuna.tsinghua.edu.cn/simple --trusted-host pypi.tuna.tsinghua.edu.cn
pip install transformers==4.11.3 azure-cognitiveservices-speech==1.43.0 -i https://pypi.tuna.tsinghua.edu.cn/simple --trusted-host pypi.tuna.tsinghua.edu.cn
```

> **Note**: PyTorch installation may take some time, please be patient. If installation fails, you can try downloading the corresponding wheel file from the PyTorch website for manual installation.

### 3. Prepare Character Images

Place the following images in the `frontend/src/main/resources/images` directory:

- `character.png` - Default character image
- `emotions/happy.png` - Happy expression
- `emotions/sad.png` - Sad expression
- `emotions/angry.png` - Angry expression
- `emotions/surprised.png` - Surprised expression
- `actions/wave.png` - Waving action
- `actions/nod.png` - Nodding action
- `actions/think.png` - Thinking action
- `actions/smile.png` - Smiling action

### 4. Build Java Project

```bash
cd backend
mvn clean install

cd ../frontend
mvn clean install
```

## Running the Project

Use the startup script to run all services:

```bash
cd scripts
.\run.bat
```

This will start the following services:
1. Python NLP Service (port 8000)
2. Python TTS Service (port 8001)
3. Spring Boot Backend (port 8080)
4. JavaFX Frontend

## Troubleshooting

### 1. Port Conflict

If you encounter port conflicts, please modify the port configuration in the following files:

- `backend/src/main/resources/application.yml` - Modify Spring Boot port
- `python/nlp/dialogpt/api.py` - Modify NLP service port
- `python/tts/azure_tts.py` - Modify TTS service port

### 2. Image Loading Failure

Ensure all image files are correctly placed in the specified directory and the file names match those referenced in the code.

### 3. Azure Speech Service Error

Check if the Azure speech service key and region are correctly set. You can directly set these values in the `application.yml` file:

```yaml
azure:
  speech:
    subscription-key: 7X46DA3q3hB6koqtOKqc2LGFHfwtZwocbU94XkGTqm3L5BEmiYSQJQQJ99BDAC3pKaRXJ3w3AAAYACOGzraO
    region: eastasia
```

### 4. DialoGPT Model Loading Failure

Ensure the DialoGPT model is correctly downloaded and placed in the specified directory: `D:\DialoGPTmaster`

### 5. JavaFX Loading Failure

If you encounter JavaFX loading issues, please check the following:

- Ensure the JavaFX SDK path is correct: `D:\javafxsdk17014`
- Check if the `JAVAFX_HOME` environment variable is correctly set
- Ensure the JavaFX configuration in `frontend/pom.xml` is correct

### 6. Python Dependency Installation Failure

If dependency installation using Chinese mirror still fails, you can try the following methods:

- Manually download and install dependencies:
  ```bash
  pip install --no-index --find-links=path/to/download/directory -r requirements.txt
  ```
- Try other Chinese mirror sources:
  - Alibaba Cloud: https://mirrors.aliyun.com/pypi/simple/
  - University of Science and Technology of China: https://pypi.mirrors.ustc.edu.cn/simple/
  - Douban: https://pypi.douban.com/simple/
- For PyTorch, you can try downloading the corresponding wheel file from the official website:
  ```bash
  # Download PyTorch wheel file
  curl -O https://download.pytorch.org/whl/cu118/torch-2.0.0%2Bcu118-cp311-cp311-win_amd64.whl
  
  # Install downloaded wheel file
  pip install torch-2.0.0+cu118-cp311-cp311-win_amd64.whl
  ```

## Custom Configuration

### Modify Character Attributes

Modify character attributes in `backend/src/main/java/com/digitalhuman/model/Character.java`.

### Modify Dialog Model Parameters

Modify DialoGPT model generation parameters in `python/nlp/dialogpt/model.py`.

### Modify Speech Synthesis Settings

Modify Azure speech synthesis settings in `backend/src/main/java/com/digitalhuman/core/SpeechService.java`. 