package com.iot.datacollector.controller;

import com.iot.datacollector.entity.DeviceData;
import com.iot.datacollector.repository.DeviceDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/data")
public class DataController {

    @Autowired
    private DeviceDataRepository repository;

    @PostMapping
    public String receiveData(@RequestBody DeviceData data) {
        data.setCreateTime(LocalDateTime.now());
        repository.save(data);
        return "Data inserted successfully";
    }
}
