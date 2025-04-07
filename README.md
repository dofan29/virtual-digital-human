# 2D Virtual Digital Human Project

This is a 2D virtual digital human project based on Java and Python, integrating natural language processing and speech synthesis capabilities.

## Technology Stack

- Backend: Spring Boot
- Frontend: JavaFX
- NLP: DialoGPT-small
- Speech Synthesis: Azure Speech Services

## System Requirements

- Java 11+
- Python 3.8+
- Maven 3.6+
- CUDA support (optional, for GPU acceleration)

## Installation Steps

1. Clone the project
```bash
git clone [project-url]
cd 2D-digital-human
```

2. Install Python dependencies
```bash
cd python
pip install -r ../docs/requirements.txt
```

3. Configure environment variables
```bash
# Azure Speech Service configuration
export AZURE_SPEECH_KEY="your-azure-speech-service-key"
export AZURE_SPEECH_REGION="your-azure-region"

# DialoGPT model path
export DIALOGPT_MODEL_PATH="D:/DialoGPTmaster"
```

4. Build Java project
```bash
mvn clean install
```

## Running the Project

Use the startup script to run all services:
```bash
cd scripts
./run.sh
```

## Project Structure

```
2D-digital-human/
├── backend/                 # Spring Boot services
├── frontend/               # JavaFX client
├── python/                 # AI services
│   ├── nlp/               # Dialogue model
│   └── tts/               # Speech synthesis
├── docs/                  # Documentation
└── scripts/               # Deployment scripts
```

## Features

- Real-time dialogue generation
- Natural speech synthesis
- 2D character rendering
- Expression and action control

## Important Notes

1. Ensure the DialoGPT model is correctly downloaded and placed in the specified directory
2. A valid Azure Speech Service subscription is required
3. First run may require downloading model files

## License

MIT License 
