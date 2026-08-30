from opcua import Client
from kafka import KafkaConsumer
import json
import time
import traceback

opc_client = Client("opc.tcp://localhost:4840")
opc_client.connect()
print("✅ 已连接 OPC UA 服务器")

temp_node = opc_client.get_node("ns=2;s=Device1.Temperature")
quality_node = opc_client.get_node("ns=2;s=Device1.Quality")
print("✅ 已获取温度节点和质量节点")

consumer = KafkaConsumer(
    'iot-data',
    bootstrap_servers='localhost:9092',
    auto_offset_reset='earliest',
    value_deserializer=lambda m: json.loads(m.decode('utf-8')),
    consumer_timeout_ms=10000
)
print("✅ 已连接到 Kafka，等待消息...")
print("🔄 开始监听 iot-data 主题...\n")

while True:
    try:
        msg = consumer.poll(timeout_ms=10000)
        if not msg:
            quality_node.set_value(0)
            print("⚠️ 10秒无数据，Quality: Bad")
            continue
        for topic_partition, records in msg.items():
            for record in records:
                data = record.value
                temp = data.get('temperature')
                if temp is not None:
                    temp_node.set_value(float(temp))
                    quality_node.set_value(1)
                    print(f"✅ 写入 OPC UA: 温度 = {temp:.2f} ℃, Quality: Good")
                else:
                    print(f"⚠️ 消息中没有 temperature 字段: {data}")
    except Exception as e:
        print("❌ 发生异常:")
        traceback.print_exc()
