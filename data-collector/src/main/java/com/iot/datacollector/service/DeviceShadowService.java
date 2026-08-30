package com.iot.datacollector.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class DeviceShadowService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final ObjectMapper mapper = new ObjectMapper();

    public void updateShadow(String deviceId, float temperature, float humidity) {
        try {
            String key = "shadow:" + deviceId;
            Map<String, Object> shadow = new HashMap<>();
            shadow.put("temperature", temperature);
            shadow.put("humidity", humidity);
            shadow.put("timestamp", LocalDateTime.now().toString());
            shadow.put("online", true);

            String json = mapper.writeValueAsString(shadow);
            redisTemplate.opsForValue().set(key, json);
        } catch (Exception e) {
            System.err.println("更新设备影子失败: " + e.getMessage());
        }
    }

    public String getShadow(String deviceId) {
        String key = "shadow:" + deviceId;
        return redisTemplate.opsForValue().get(key);
    }
}
