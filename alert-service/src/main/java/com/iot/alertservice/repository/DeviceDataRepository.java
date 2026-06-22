package com.iot.alertservice.repository;

import com.iot.alertservice.entity.DeviceData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DeviceDataRepository extends JpaRepository<DeviceData, Long> {
    @Query(value = "SELECT * FROM device_data ORDER BY create_time DESC LIMIT 1", nativeQuery = true)
    Optional<DeviceData> findLatestRecord();
}
