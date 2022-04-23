package com.example.demo.scheduledTask;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.bean.MeasuringPoints;
import com.example.demo.bean.RsiData;
import com.example.demo.mapper.MeasuringPointsDataMapper;
import com.example.demo.mapper.MeasuringPointsMapper;
import com.example.demo.mapper.RsiDataMapper;
import com.example.demo.utils.TimeUtils;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.*;

public class SchedudedTasks {
    @Autowired
    private MeasuringPointsMapper measuringPointsMapper;
    @Autowired
    private RsiDataMapper rsiDataMapper;
    //private List<String> activateMeasuringPoints

    @Async
    @Scheduled(cron = "0 0 2 1 * ? *")
    public void monthlyReporting(){


    }


    @Async
    @Scheduled(cron="0 4/5 * * * * *")
    public void vantagePointsMonitor(){
        Date date=new Date();
        String currentTimeStamp=TimeUtils.dateFormat.format(date);
        int currentMinute=Integer.parseInt(currentTimeStamp.substring(14,16));
        int currentSecond=Integer.parseInt(currentTimeStamp.substring(17,19));
        int temp=((currentMinute%5)*60+currentSecond)*1000;
        String timestampLeft=TimeUtils.dateFormat.format(new Date(date.getTime()-temp));
        String timestampRight=TimeUtils.dateFormat.format(new Date().getTime()-temp+300000);
        List<MeasuringPoints> measuringPoints=measuringPointsMapper.selectList(new QueryWrapper<MeasuringPoints>()
                .ne("status","abandoned")
        );
        List<RsiData> latestData=rsiDataMapper.selectList(new QueryWrapper<RsiData>()
                .eq("type",'A')
                .between("timestamp_",timestampLeft,timestampRight)
        );
        Set<String> onlinePoints=new HashSet<>();
        for(RsiData data:latestData){
            onlinePoints.add(data.getProvider());
        }
        for(MeasuringPoints point:measuringPoints){
            String status=point.getStatus();
            String identification=point.getIdentification();
            if(status.equals("normal")){
                if(!onlinePoints.contains(identification)){
                    point.setStatus("1lost");
                    measuringPointsMapper.updateById(point);
                }
            }else if(status.equals("1lost")){
                if(onlinePoints.contains(identification)){
                    point.setStatus("normal");
                    measuringPointsMapper.updateById(point);
                }else {
                    point.setStatus("2lost");
                    measuringPointsMapper.updateById(point);
                }
            }else if(status.equals("2lost")){
                if(onlinePoints.contains(identification)){
                    point.setStatus("normal");
                    measuringPointsMapper.updateById(point);
                }else {
                    point.setStatus("offline");
                    measuringPointsMapper.updateById(point);
                }
            }else if(status.equals("offline")){
                if(onlinePoints.contains(identification)){
                    point.setStatus("normal");
                    measuringPointsMapper.updateById(point);
                }
            }
        }
    }


}
