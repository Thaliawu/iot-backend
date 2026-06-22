package com.iot.alertservice.service;

import com.iot.alertservice.entity.DeviceData;
import com.iot.alertservice.repository.DeviceDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class AlertService {

    @Autowired
    private DeviceDataRepository repository;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${alert.temperature.threshold:30.0}")
    private double temperatureThreshold;

    @Value("${alert.silence.seconds:3600}")
    private long silenceSeconds;

    private static final String REDIS_KEY_LAST_ALERT = "alert:last_temperature_time";

    @Scheduled(fixedDelay = 60000)
    public void checkTemperatureAndAlert() {
        DeviceData latest = repository.findLatestRecord().orElse(null);
        if (latest == null) {
            System.out.println("暂无数据，跳过告警检查");
            return;
        }
        double temp = latest.getTemperature();
        if (temp > temperatureThreshold) {
            String lastAlertTimeStr = redisTemplate.opsForValue().get(REDIS_KEY_LAST_ALERT);
            if (lastAlertTimeStr != null) {
                LocalDateTime lastAlertTime = LocalDateTime.parse(lastAlertTimeStr);
                if (Duration.between(lastAlertTime, LocalDateTime.now()).getSeconds() < silenceSeconds) {
                    System.out.println("告警静默期内，跳过发送邮件。上次告警时间：" + lastAlertTime);
                    return;
                }
            }
            sendAlertEmail(temp, latest.getHumidity(), latest.getCreateTime());
            redisTemplate.opsForValue().set(REDIS_KEY_LAST_ALERT, LocalDateTime.now().toString());
        } else {
            System.out.println("温度正常: " + temp + " ℃，未触发告警");
        }
    }

    private void sendAlertEmail(double temp, double hum, LocalDateTime time) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("1127758625@qq.com");
        message.setTo("1127758625@qq.com");
        message.setSubject("【设备告警】温度超过阈值");
        message.setText(String.format("设备在 %s 检测到异常温度: %.1f ℃ (湿度: %.1f %%)，超过阈值 %.1f ℃。请及时处理。", time, temp, hum, temperatureThreshold));
        mailSender.send(message);
        System.out.println("告警邮件已发送，温度: " + temp + " ℃");
    }
}
