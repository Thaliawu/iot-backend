from opcua import Client
from kafka import KafkaConsumer
import json
import time
import traceback

def get_opcua_client():
    client = Client("opc.tcp://localhost:4840")
    client.connect()
    print("✅ OPC UA客户端连接成功")
    return client

opc_client = get_opcua_client()
# 使用正确的节点ID（从探查脚本获取）
temp_node = opc_client.get_node("ns=2;i=2")      # 温度节点
quality_node = opc_client.get_node("ns=2;i=3")   # 质量节点
print("✅ 已获取温度节点和质量节点")

consumer = KafkaConsumer(
    'iot-data',
    bootstrap_servers='localhost:9092',
    auto_offset_reset='earliest',
    value_deserializer=lambda m: json.loads(m.decode('utf-8')),
    consumer_timeout_ms=10000
)
print("✅ 已连接到Kafka，等待消息...")
print("🔄 开始监听 iot-data 主题...\n")

while True:
    try:
        msg_pack = consumer.poll(timeout_ms=10000)
        if not msg_pack:
            quality_node.set_value(0)
            print("⚠️ 10秒无数据，Quality: Bad")
            continue
        for tp, messages in msg_pack.items():
            for msg in messages:
                data = msg.value
                temp = data.get('temperature')
                if temp is None:
                    print(f"⚠️ 数据中无温度字段: {data}")
                    continue
                try:
                    temp_node.set_value(float(temp))
                    quality_node.set_value(1)
                    print(f"✅ 写入OPC UA: {temp:.2f}℃, Quality: Good")
                except Exception as write_error:
                    print(f"❌ 写入OPC UA失败: {write_error}")
                    try:
                        opc_client.disconnect()
                        opc_client = get_opcua_client()
                        temp_node = opc_client.get_node("ns=2;i=2")
                        quality_node = opc_client.get_node("ns=2;i=3")
                        temp_node.set_value(float(temp))
                        quality_node.set_value(1)
                        print(f"✅ 重试成功: {temp:.2f}℃, Quality: Good")
                    except Exception as reconnect_error:
                        print(f"❌ 重连失败: {reconnect_error}")
    except Exception as e:
        print(f"❌ 主循环异常: {e}")
        traceback.print_exc()
        time.sleep(2)
