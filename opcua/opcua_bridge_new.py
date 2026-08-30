from opcua import Client
from kafka import KafkaConsumer
import json
import time
import traceback

def get_opcua_client():
    """创建并连接OPC UA客户端"""
    client = Client("opc.tcp://localhost:4840")
    client.connect()
    print("✅ OPC UA客户端连接成功")
    return client

# 初始化OPC UA客户端
opc_client = get_opcua_client()
temp_node = opc_client.get_node("ns=2;s=Device1.Temperature")
quality_node = opc_client.get_node("ns=2;s=Device1.Quality")

# Kafka消费者
consumer = KafkaConsumer(
    'iot-data',
    bootstrap_servers='localhost:9092',
    auto_offset_reset='earliest',
    value_deserializer=lambda m: json.loads(m.decode('utf-8')),
    consumer_timeout_ms=10000
)
print("✅ 已连接到Kafka")

while True:
    try:
        # 1. 从Kafka拉取消息
        msg_pack = consumer.poll(timeout_ms=10000)

        if not msg_pack:
            # 没有消息：质量位设为Bad (0)
            quality_node.set_value(0)
            print("⚠️ 10秒无数据，Quality: Bad")
            continue

        # 2. 处理消息
        for tp, messages in msg_pack.items():
            for msg in messages:
                data = msg.value
                temp = data.get('temperature')
                if temp is None:
                    print(f"⚠️ 数据中无温度字段: {data}")
                    continue

                # 3. 写入OPC UA（带重试逻辑）
                try:
                    temp_node.set_value(float(temp))
                    quality_node.set_value(1)
                    print(f"✅ 写入OPC UA: {temp:.2f}℃, Quality: Good")
                except Exception as write_error:
                    print(f"❌ 写入OPC UA失败: {write_error}")
                    # 写入失败，尝试重连
                    try:
                        opc_client.disconnect()
                        opc_client = get_opcua_client()
                        temp_node = opc_client.get_node("ns=2;s=Device1.Temperature")
                        quality_node = opc_client.get_node("ns=2;s=Device1.Quality")
                        print("🔄 已重新连接OPC UA")
                        # 重连后立即重试写入
                        temp_node.set_value(float(temp))
                        quality_node.set_value(1)
                        print(f"✅ 重试成功: {temp:.2f}℃, Quality: Good")
                    except Exception as reconnect_error:
                        print(f"❌ 重连失败: {reconnect_error}")

    except Exception as e:
        print(f"❌ 主循环异常: {e}")
        traceback.print_exc()
        time.sleep(2)
