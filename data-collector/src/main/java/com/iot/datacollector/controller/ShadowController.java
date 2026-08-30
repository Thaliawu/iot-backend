package com.iot.datacollector.controller;

import com.iot.datacollector.service.DeviceShadowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shadow")
public class ShadowController {

    @Autowired
    private DeviceShadowService shadowService;

    @GetMapping
    public String getShadow(@RequestParam(defaultValue = "dev1") String deviceId) {
        String shadow = shadowService.getShadow(deviceId);
        if (shadow == null) {
            return "{}";
        }
        return shadow;
    }
}
