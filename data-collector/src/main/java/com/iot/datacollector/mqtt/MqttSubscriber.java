package com.iot.datacollector.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.datacollector.entity.DeviceData;
import com.iot.datacollector.repository.DeviceDataRepository;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class MqttSubscriber {

    private static final String BROKER = "tcp://localhost:1883";
    private static final String TOPIC = "devices/dev1/telemetry";
    private static final String CLIENT_ID = "data-collector-subscriber";

    @Autowired
    private DeviceDataRepository repository;

    private MqttClient client;

    @EventListener(ApplicationReadyEvent.class)
    public void init() throws MqttException {
        client = new MqttClient(BROKER, CLIENT_ID);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setAutomaticReconnect(true);
        client.connect(options);
        System.out.println("MQTT 订阅者已连接到 Broker");

        // 订阅主题
        client.subscribe(TOPIC, (topic, message) -> {
            String payload = new String(message.getPayload());
            System.out.println("收到 MQTT 消息: " + payload);
            saveToDatabase(payload);
        });

        System.out.println("已订阅主题: " + TOPIC);
    }

    private void saveToDatabase(String payload) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> data = mapper.readValue(payload, Map.class);

            DeviceData deviceData = new DeviceData();
            deviceData.setTemperature(((Number) data.get("temperature")).floatValue());
            deviceData.setHumidity(((Number) data.get("humidity")).floatValue());
            deviceData.setCreateTime(LocalDateTime.now());

            repository.save(deviceData);
            System.out.println("数据已存入数据库: " + payload);
        } catch (Exception e) {
            System.err.println("解析或保存数据失败: " + e.getMessage());
        }
    }
}
