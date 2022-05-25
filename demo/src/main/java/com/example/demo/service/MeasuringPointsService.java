package com.example.demo.service;

import java.net.UnknownHostException;

public interface MeasuringPointsService {
    /**
     * 更新探测节点状态表，同时同步数据库的状态数据
     */
    public void updateMeasuringPointsStatus();

    /**
     * 获取测量节点详细信息
     * @return json
     */
    public String getMeasuringPointsData() throws UnknownHostException;

    public String addMeasuringPoint(String json);

    public String editMeasuringPoint(String json);

    public String deleteMeasuringPoint(String json);
}
