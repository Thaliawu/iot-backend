from opcua import Client
import sys

url = "opc.tcp://localhost:4840"
client = Client(url)

try:
    client.connect()
    print("✅ 已连接到 OPC UA 服务器\n")
    print("正在浏览服务器节点结构...")
    print("-" * 50)

    root = client.get_root_node()
    print(f"根节点: {root}")

    objects = client.get_objects_node()
    print(f"对象节点: {objects}")

    # 查看对象节点下的所有子节点
    children = objects.get_children()
    for child in children:
        print(f"  子节点: {child}, BrowseName: {child.get_browse_name()}")

    # 尝试找到 Device1
    device_node = None
    for child in children:
        if "Device1" in str(child.get_browse_name()):
            device_node = child
            break

    if device_node:
        print(f"\n✅ 找到设备节点: {device_node}")
        print(f"   BrowseName: {device_node.get_browse_name()}")
        # 查看设备节点下的子节点
        device_children = device_node.get_children()
        for sub in device_children:
            print(f"     子节点: {sub}, BrowseName: {sub.get_browse_name()}")
    else:
        print("\n⚠️ 未找到 Device1 节点")
        # 如果没找到，打印所有顶级对象作为参考
        print("\n所有顶级对象:")
        for child in children:
            print(f"  {child.get_browse_name()}")

    client.disconnect()
    print("\n✅ 浏览完成")

except Exception as e:
    print(f"❌ 连接失败: {e}")
