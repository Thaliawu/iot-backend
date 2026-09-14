# Kubernetes 故障演练报告

## 演练目标
验证 K8s 集群在故障场景下的自愈能力，确保工业物联网系统的可靠性。

## 演练环境
- Kubernetes: k3s v1.36.4
- 节点: thalia-virtual-machine (control-plane)
- 部署服务: PostgreSQL、Redis、Mosquitto、data-collector、alert-service
- HPA 配置: CPU 50%，1-5 个 Pod

## 演练一：Pod 故障自愈

### 操作
手动删除一个 data-collector Pod：

```bash
kubectl delete pod data-collector-59cfdf76c9-7lztb
