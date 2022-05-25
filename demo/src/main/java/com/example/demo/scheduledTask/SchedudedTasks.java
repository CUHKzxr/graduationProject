package com.example.demo.scheduledTask;

import com.example.demo.mapper.RsiDataMapper;
import com.example.demo.service.MeasuringPointsService;
import com.example.demo.service.ReportService;
import com.example.demo.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public class SchedudedTasks {

    @Autowired
    private RsiDataMapper rsiDataMapper;
    @Qualifier("MeasuringPointsService")
    @Autowired
    private MeasuringPointsService measuringPointsService;
    @Qualifier("ReportService")
    @Autowired
    private ReportService reportService;

    @Async
    @Scheduled(cron = "0 0 0 0 * ? *")
    public void dailyReporting(){
        Date date=new Date();
        Date preDate=new Date(date.getTime()-8640000);
        String dateString=TimeUtils.dateFormat.format(preDate).substring(0,10);
        reportService.generateDailyReport(dateString);
    }


    @Async
    @Scheduled(cron="0 4/5 * * * * *")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public void measuringPointsMonitor(){
        measuringPointsService.updateMeasuringPointsStatus();
    }


}
