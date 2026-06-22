package com.iot.datacollector.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "device_data_avg")
public class DeviceDataAvg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double avgTemperature;
    private Double avgHumidity;
    private LocalDateTime statTime;

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getAvgTemperature() { return avgTemperature; }
    public void setAvgTemperature(Double avgTemperature) { this.avgTemperature = avgTemperature; }
    public Double getAvgHumidity() { return avgHumidity; }
    public void setAvgHumidity(Double avgHumidity) { this.avgHumidity = avgHumidity; }
    public LocalDateTime getStatTime() { return statTime; }
    public void setStatTime(LocalDateTime statTime) { this.statTime = statTime; }
}
