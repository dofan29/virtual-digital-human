from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import logging
from model import DialogGPTModel

# 配置日志
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

app = FastAPI()

# 初始化模型
model = DialogGPTModel()
logger.info("DialoGPT模型加载完成")

class ChatRequest(BaseModel):
    message: str
    context: str = ""

class ChatResponse(BaseModel):
    response: str

@app.post("/chat", response_model=ChatResponse)
async def chat(request: ChatRequest):
    try:
        logger.info(f"收到聊天请求: {request.message}")
        logger.info(f"上下文: {request.context}")
        
        response = model.generate_response(request.message, request.context)
        logger.info(f"生成回复: {response}")
        
        return ChatResponse(response=response)
    except Exception as e:
        logger.error(f"处理请求时发生错误: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/health")
async def health_check():
    return {"status": "healthy"} 