import requests
import json

def test_health():
    try:
        response = requests.get("http://localhost:8000/health")
        print(f"健康检查状态码: {response.status_code}")
        print(f"响应内容: {response.text}")
        return response.status_code == 200
    except Exception as e:
        print(f"健康检查失败: {str(e)}")
        return False

def test_chat():
    try:
        headers = {"Content-Type": "application/json"}
        data = {"message": "你好", "context": ""}
        response = requests.post(
            "http://localhost:8000/chat",
            headers=headers,
            data=json.dumps(data)
        )
        print(f"聊天请求状态码: {response.status_code}")
        print(f"响应内容: {response.text}")
        return response.status_code == 200
    except Exception as e:
        print(f"聊天请求失败: {str(e)}")
        return False

if __name__ == "__main__":
    print("测试NLP服务...")
    if test_health():
        print("健康检查成功")
        test_chat()
    else:
        print("健康检查失败，请确保服务正在运行") 