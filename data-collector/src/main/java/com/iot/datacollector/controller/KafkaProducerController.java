package com.iot.datacollector.controller;

import com.iot.datacollector.service.MockKafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
public class KafkaProducerController {

    @Autowired
    private MockKafkaService mockKafkaService;

    @GetMapping("/send")
    public String sendMessage(@RequestParam("msg") String message) {
        mockKafkaService.send(message);
        return "消息已发送到模拟 Kafka: " + message;
    }
}
