package com.example.demo.controler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.bean.MeasuringPointsData;
import com.example.demo.bean.MeasuringTask;
import com.example.demo.mapper.MeasuringPointsDataMapper;
import com.example.demo.utils.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/measuringPoints")
public class MeasuringPointsControler {
    @Autowired
    private MeasuringPointsDataMapper measuringPointsDataMapper;

    @PostMapping("/getData")
    public String getData( @RequestBody String requestData){
        try {
            List<MeasuringPointsData> list=measuringPointsDataMapper.queryMeasuringPointsData(new QueryWrapper<>());
            List<Map<String,Object>> resultList=new ArrayList<Map<String,Object>>();
            String preIdentification=null;
            Map<String,Object> preMap=null;
            List<MeasuringTask> preTasks=null;
            for(MeasuringPointsData data:list){
                if(data.getIdentification().equals(preIdentification)){
                    MeasuringTask temp=new MeasuringTask(data.getId(),data.getIdentification(),data.isEffective()
                            ,data.getStartTimestamp(),data.getEndTimestamp(),data.getText());
                    preTasks.add(temp);
                }else {
                    preIdentification=data.getIdentification();
                    preTasks=new ArrayList<MeasuringTask>(){{
                        add(new MeasuringTask(data.getId(),data.getIdentification(),data.isEffective()
                                ,data.getStartTimestamp(),data.getEndTimestamp(),data.getText()));
                    }};
                    List<MeasuringTask> finalPreTasks = preTasks;
                    preMap=new HashMap<String,Object>(){{
                        put("identification",data.getIdentification());
                        put("location",data.getLocation());
                        put("ipv4", data.getIpv4Address());
                        put("ipv6",IpUtils.ipv6ToString(data.getIpv6Address()));
                        put("status",data.getStatus());
                        put("tasks", finalPreTasks);
                    }};
                    resultList.add(preMap);
                }
            }
            String jsonString= JSON.toJSONString(new HashMap<String,Object>(){{
                put("code",200);
                put("data",resultList);
            }});
            System.out.println(jsonString);
            return jsonString;
        }catch (UnknownHostException e){
            e.printStackTrace();
            return null;
        }

    }
}
