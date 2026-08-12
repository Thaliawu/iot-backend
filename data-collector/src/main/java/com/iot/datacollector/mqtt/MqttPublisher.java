package com.iot.datacollector.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import java.util.Random;

@Component
public class MqttPublisher {

    private static final String BROKER = "tcp://localhost:1883";
    private static final String TOPIC = "devices/dev1/telemetry";
    private static final String CLIENT_ID = "data-collector-publisher";

    private MqttClient client;
    private Random random = new Random();

    @EventListener(ApplicationReadyEvent.class)
    public void init() throws MqttException {
        client = new MqttClient(BROKER, CLIENT_ID);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setAutomaticReconnect(true);
        client.connect(options);
        System.out.println("MQTT 已连接到 Broker");

        new Thread(() -> {
            while (true) {
                try {
                    publishData();
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void publishData() throws MqttException {
        double temp = 20 + random.nextDouble() * 10;
        double hum = 40 + random.nextDouble() * 40;
        String payload = String.format("{\"temperature\":%.1f,\"humidity\":%.1f}", temp, hum);
        MqttMessage message = new MqttMessage(payload.getBytes());
        message.setQos(1);
        client.publish(TOPIC, message);
        System.out.println("发布消息: " + payload);
    }
}
