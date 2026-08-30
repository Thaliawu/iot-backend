from opcua import Client
import time

url = "opc.tcp://localhost:4840"
client = Client(url)

try:
    client.connect()
    print("✅ 已连接到 OPC UA 服务器")

    # 获取温度节点 (ns=2, i=2 是默认生成的节点ID)
    # 如果连接成功但读不到数据，可能是节点路径不同，用 get_node 配合路径
    temp_node = client.get_node("ns=2;i=2")
    
    for i in range(10):
        val = temp_node.get_value()
        print(f"温度: {val:.2f} ℃")
        time.sleep(1)
        
    client.disconnect()
    print("✅ 断开连接")
    
except Exception as e:
    print(f"❌ 连接失败: {e}")
    client.disconnect()
