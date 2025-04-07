import requests
import json

def test_chat():
    url = "http://localhost:8000/chat"
    headers = {"Content-Type": "application/json"}
    data = {
        "message": "你好",
        "context": ""
    }
    
    try:
        response = requests.post(url, headers=headers, json=data)
        print(f"状态码: {response.status_code}")
        print(f"响应内容: {response.text}")
        
        if response.status_code == 200:
            result = response.json()
            print(f"\n回复: {result['response']}")
    except Exception as e:
        print(f"请求失败: {str(e)}")

if __name__ == "__main__":
    print("测试聊天功能...")
    test_chat() 