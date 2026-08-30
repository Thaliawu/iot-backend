from opcua import Server
import time
import random

server = Server()
server.set_endpoint("opc.tcp://0.0.0.0:4840")
server.set_server_name("IoT OPC UA Server")

uri = "http://iot.opcua"
idx = server.register_namespace(uri)

objects = server.get_objects_node()
device = objects.add_object(idx, "Device1")

# 温度节点
temp_var = device.add_variable(idx, "Temperature", 0.0)
temp_var.set_writable(True)

# 数据质量位节点（Good=1, Bad=0）
quality_var = device.add_variable(idx, "Quality", 0)
quality_var.set_writable(True)

print("OPC UA 服务器启动，端口 4840")
print("温度节点: Device1.Temperature")
print("质量节点: Device1.Quality (1=Good, 0=Bad)")

server.start()

try:
    while True:
        temp = 20 + random.random() * 10
        temp_var.set_value(temp)
        quality_var.set_value(1)
        print(f"温度更新: {temp:.2f} ℃, Quality: Good")
        time.sleep(2)
except KeyboardInterrupt:
    print("服务器停止")
    server.stop()
