package com.iot.datacollector.controller;

import com.iot.datacollector.entity.DeviceData;
import com.iot.datacollector.mapper.DeviceDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/data")
public class DataController {

    @Autowired
    private DeviceDataMapper deviceDataMapper;

    @PostMapping
    public String receiveData(@RequestBody DeviceData data) {
        data.setCreateTime(LocalDateTime.now());
        int rows = deviceDataMapper.insert(data);
        return rows > 0 ? "Data inserted successfully" : "Insert failed";
    }
}
