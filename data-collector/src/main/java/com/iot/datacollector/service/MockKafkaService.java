package com.iot.datacollector.service;

import org.springframework.stereotype.Service;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Service
public class MockKafkaService {

    private BlockingQueue<String> queue = new ArrayBlockingQueue<>(100);

    @EventListener(ApplicationReadyEvent.class)
    public void startConsumer() {
        new Thread(() -> {
            while (true) {
                try {
                    String message = queue.take();
                    System.out.println("✅ 从模拟 Kafka 消费到消息: " + message);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }

    public void send(String message) {
        try {
            queue.put(message);
            System.out.println("📤 发送消息到模拟 Kafka: " + message);
        } catch (InterruptedException e) {
            System.err.println("发送失败: " + e.getMessage());
        }
    }
}
