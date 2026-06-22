package com.iot.datacollector.repository;

import com.iot.datacollector.entity.DeviceData;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface DeviceDataRepository extends JpaRepository<DeviceData, Long> {
    List<DeviceData> findByCreateTimeBetween(LocalDateTime start, LocalDateTime end);
}
