package com.iot.datacollector.repository;

import com.iot.datacollector.entity.DeviceDataAvg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceDataAvgRepository extends JpaRepository<DeviceDataAvg, Long> {
}
