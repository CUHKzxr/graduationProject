package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.example.demo.entity.MeasuringPointsData;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MeasuringPointsDataMapper extends BaseMapper<MeasuringPointsData> {
    public List<MeasuringPointsData> queryMeasuringPointsData(@Param(Constants.WRAPPER) QueryWrapper<MeasuringPointsData> wrapper);
}
