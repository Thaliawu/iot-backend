package com.iot.datacollector.service;

import com.iot.datacollector.entity.DeviceData;
import com.iot.datacollector.entity.DeviceDataAvg;
import com.iot.datacollector.repository.DeviceDataAvgRepository;
import com.iot.datacollector.repository.DeviceDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@EnableScheduling
public class AggregationService {

    @Autowired
    private DeviceDataRepository deviceDataRepository;

    @Autowired
    private DeviceDataAvgRepository deviceDataAvgRepository;

    // 每分钟执行一次（每分钟的第0秒）
    @Scheduled(cron = "0 * * * * *")
    public void aggregateLastMinute() {
        LocalDateTime endTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime startTime = endTime.minusMinutes(1);
        List<DeviceData> records = deviceDataRepository.findByCreateTimeBetween(startTime, endTime);
        if (!records.isEmpty()) {
            double avgTemp = records.stream().mapToDouble(DeviceData::getTemperature).average().getAsDouble();
            double avgHum = records.stream().mapToDouble(DeviceData::getHumidity).average().getAsDouble();
            DeviceDataAvg avgRecord = new DeviceDataAvg();
            avgRecord.setAvgTemperature(avgTemp);
            avgRecord.setAvgHumidity(avgHum);
            avgRecord.setStatTime(endTime);
            deviceDataAvgRepository.save(avgRecord);
            System.out.println("聚合记录已保存: " + endTime + " 平均温度=" + avgTemp + " 平均湿度=" + avgHum);
        } else {
            System.out.println("无数据，跳过聚合: " + endTime);
        }
    }
}
