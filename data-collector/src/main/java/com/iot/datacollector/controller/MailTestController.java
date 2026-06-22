package com.iot.datacollector.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class MailTestController {

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/sendmail")
    public String sendMail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("1127758625@qq.com");
        message.setTo("1127758625@qq.com"); // 可以改成你自己的邮箱
        message.setSubject("Grafana告警测试");
        message.setText("这是一封来自Spring Boot的测试邮件，说明邮件配置正确。");
        mailSender.send(message);
        return "邮件已发送，请检查邮箱";
    }
}
