package com.iot.datacollector.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iot.datacollector.entity.DeviceData;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeviceDataMapper extends BaseMapper<DeviceData> {
}
