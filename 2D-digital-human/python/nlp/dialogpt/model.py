import os
import logging
import openai
import traceback
import time

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

class DialogGPTModel:
    def __init__(self):
        # 配置环境变量
        openai.api_key = "your API Key"
        openai.api_base = "your api_base URL"
        self.model_id = 'your model_id'
        logger.info("讯飞星火大模型API客户端初始化完成")

    def generate_response(self, prompt, context="", max_retries=3):
        for attempt in range(max_retries):
            try:
                messages = []
                if context:
                    messages.append({
                        'role': 'system',
                        'content': f'你是一个友好的AI助手。请用中文回答用户的问题。上下文：{context}'
                    })
                else:
                    messages.append({
                        'role': 'system',
                        'content': '你是一个友好的AI助手。请用中文回答用户的问题。'
                    })
                messages.append({
                    'role': 'user',
                    'content': prompt
                })
                
                logger.info(f"发送请求到讯飞星火大模型API，模型: {self.model_id}")
                logger.info(f"请求内容: {messages}")
                
                response = openai.ChatCompletion.create(
                    model=self.model_id,
                    messages=messages,
                    stream=False,
                    temperature=0.7,
                    max_tokens=4096,
                    extra_headers={"lora_id": "0"},
                    stream_options={"include_usage": True},
                    extra_body={"show_ref_label": True}
                )
                
                logger.info(f"收到API响应: {response}")
                
                if not response or not response.choices:
                    raise Exception("API返回的响应格式不正确")
                
                return response.choices[0].message.content
                
            except Exception as e:
                logger.error(f"第{attempt + 1}次尝试生成回复时发生错误: {str(e)}")
                logger.error(f"错误详情: {traceback.format_exc()}")
                if attempt < max_retries - 1:
                    wait_time = (attempt + 1) * 5
                    logger.info(f"等待{wait_time}秒后重试...")
                    time.sleep(wait_time)
                else:
                    return self._fallback_response(prompt)

    def _fallback_response(self, prompt):
        """简单的回退响应机制"""
        responses = [
            "抱歉，我现在遇到了一些技术问题，能稍后再试吗？",
            "系统正在维护中，请稍后再试。",
            "我需要一点时间来处理这个请求，请稍后再试。",
            "当前网络可能不太稳定，请稍后再试。",
            "抱歉，服务暂时不可用，请稍后再试。"
        ]
        return responses[hash(prompt) % len(responses)] 