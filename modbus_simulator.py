import requests
import time
import random

url = "http://localhost:8080/api/data"

while True:
    temperature = round(random.uniform(20.0, 30.0), 1)
    humidity = round(random.uniform(40.0, 80.0), 1)
    payload = {"temperature": temperature, "humidity": humidity}
    try:
        response = requests.post(url, json=payload)
        print(f"发送: {payload} -> 响应: {response.text}")
    except Exception as e:
        print(f"错误: {e}")
    time.sleep(5)
