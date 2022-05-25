package com.example.demo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.MeasuringTask;
import com.example.demo.entity.MeasuringPoint;
import com.example.demo.entity.RsiData;
import com.example.demo.entity.MeasuringPointsData;
import com.example.demo.mapper.MeasuringPointsDataMapper;
import com.example.demo.mapper.MeasuringPointsMapper;
import com.example.demo.mapper.RsiDataMapper;
import com.example.demo.utils.IpUtils;
import com.example.demo.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.util.*;

@Service("MeasuringPointsService")
public class MeasuringPointsServiceImp implements MeasuringPointsService {

    public static List<MeasuringPoint> measuringPoints =null;


    @Autowired
    private MeasuringPointsMapper measuringPointsMapper;

    @Autowired
    private RsiDataMapper rsiDataMapper;

    @Autowired
    private MeasuringPointsDataMapper measuringPointsDataMapper;


    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
    public void updateMeasuringPointsStatus() {
        Date date=new Date();
        String currentTimeStamp= TimeUtils.dateFormat.format(date);
        int currentMinute=Integer.parseInt(currentTimeStamp.substring(14,16));
        int currentSecond=Integer.parseInt(currentTimeStamp.substring(17,19));
        int temp=((currentMinute%5)*60+currentSecond)*1000;
        String timestampLeft=TimeUtils.dateFormat.format(new Date(date.getTime()-temp));
        String timestampRight=TimeUtils.dateFormat.format(new Date().getTime()-temp+300000);
        List<MeasuringPoint> measuringPointList = measuringPointsMapper.selectList(new QueryWrapper<MeasuringPoint>()
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
        for(MeasuringPoint point: measuringPointList){
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
            updateMeasuringPointListFromDatabase();
        }
    }

    @Override
    public String getMeasuringPointsData() throws UnknownHostException {
        if(measuringPoints ==null){
            updateMeasuringPointListFromDatabase();
        }
        List<Map<String,Object>> resultList=new ArrayList<Map<String,Object>>();
        for(MeasuringPoint point: measuringPoints){
            Map<String,Object> rMap=new HashMap<String,Object>(){{
                put("identification",point.getIdentification());
                put("location",point.getLocation());
                put("ipv4", point.getIpv4Address());
                put("ipv6", IpUtils.ipv6ToString(point.getIpv6Address()));
                put("status",point.getStatus());
                put("tasks", point.getTasks());
            }};
            resultList.add(rMap);
        }
        String jsonString= JSON.toJSONString(new HashMap<String,Object>(){{
            put("code",200);
            put("data",resultList);
        }});
        System.out.println(jsonString);
        return jsonString;
    }

    @Override
    public String addMeasuringPoint(String json) {
        try {
            JSONObject request=JSON.parseObject(json);
            JSONObject point=request.getJSONObject("point");
            String provider=point.getString("provider");
            String location=point.getString("location");
            int ipv4=IpUtils.parseIntIpv4(point.getString("IPv4Address"));
            byte[] ipv6 = IpUtils.parseIpv6(point.getString("IPv6Address"));
            MeasuringPoint measuringPoint =new MeasuringPoint(provider,location,ipv4,ipv6,"offline");
            MeasuringPoint v = measuringPointsMapper.selectById(provider);
            if(v!=null){
                return JSON.toJSONString(new HashMap<String,String>(){{put("result","existed");}});
            }
            int r= measuringPointsMapper.insert(measuringPoint);
            if(r==1){
                updateMeasuringPointListFromDatabase();
                return JSON.toJSONString(new HashMap<String,String>(){{put("result","success");}});
            }else {
                return JSON.toJSONString(new HashMap<String,String>(){{put("result","error");}});
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JSON.toJSONString(new HashMap<String,String>(){{put("result","error");}});
        }

    }

    @Override
    public String editMeasuringPoint(String json) {
        try {
            JSONObject request=JSON.parseObject(json);
            JSONObject point=request.getJSONObject("point");
            String provider=point.getString("provider");
            String location=point.getString("location");
            int ipv4=IpUtils.parseIntIpv4(point.getString("IPv4Address"));
            byte[] ipv6 = IpUtils.parseIpv6(point.getString("IPv6Address"));
            MeasuringPoint measuringPoint =new MeasuringPoint(provider,location,ipv4,ipv6,"offline");
            MeasuringPoint v = measuringPointsMapper.selectById(provider);
            if(v==null){
                return JSON.toJSONString(new HashMap<String,String>(){{put("result","none");}});
            }
            int r= measuringPointsMapper.updateById(measuringPoint);
            if(r==1){
                updateMeasuringPointListFromDatabase();
                return JSON.toJSONString(new HashMap<String,String>(){{put("result","success");}});
            }else {
                return JSON.toJSONString(new HashMap<String,String>(){{put("result","nothing changed");}});
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JSON.toJSONString(new HashMap<String,String>(){{put("result","error");}});
        }
    }

    @Override
    public String deleteMeasuringPoint(String json) {
        try {
            JSONObject request=JSON.parseObject(json);
            JSONObject point=request.getJSONObject("point");
            String provider=point.getString("provider");
            MeasuringPoint v = measuringPointsMapper.selectById(provider);
            if(v==null){
                return JSON.toJSONString(new HashMap<String,String>(){{put("result","none");}});
            }
            int r= measuringPointsMapper.deleteById(provider);
            if(r==1){
                updateMeasuringPointListFromDatabase();
                return JSON.toJSONString(new HashMap<String,String>(){{put("result","success");}});
            }else {
                return JSON.toJSONString(new HashMap<String,String>(){{put("result","nothing changed");}});
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JSON.toJSONString(new HashMap<String,String>(){{put("result","error");}});
        }
    }

    public void updateMeasuringPointListFromDatabase(){
        List<MeasuringPointsData> list= measuringPointsDataMapper.queryMeasuringPointsData(new QueryWrapper<>());
        measuringPoints =new ArrayList<>();
        String preIdentification=list.get(0).getIdentification();
        MeasuringPointsData preData=list.get(0);
        Map<String,Object> preMap=new HashMap<>();
        List<MeasuringTask> preTasks=new ArrayList<>();
        for(MeasuringPointsData data:list){
            if(!data.getIdentification().equals(preIdentification)){
                measuringPoints.add(new MeasuringPoint(preData.getIdentification(),preData.getLocation(), preData.getIpv4Address()
                        , preData.getIpv6Address(), preData.getStatus(),preTasks));
                preIdentification=data.getIdentification();
                preTasks=new ArrayList<MeasuringTask>();
                preData=data;
            }
            if(data.isEffective()){
                MeasuringTask temp=new MeasuringTask(data.getId(),data.getIdentification(),data.isEffective()
                        ,data.getStartTimestamp(),data.getEndTimestamp(),data.getText());
                preTasks.add(temp);
            }
        }
        measuringPoints.add(new MeasuringPoint(preData.getIdentification(),preData.getLocation(), preData.getIpv4Address()
                , preData.getIpv6Address(), preData.getStatus(),preTasks));
    }

    private String getStatusJsonFromDatabase() throws UnknownHostException {
        List<MeasuringPointsData> list= measuringPointsDataMapper.queryMeasuringPointsData(new QueryWrapper<>());
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
                    put("ipv6", IpUtils.ipv6ToString(data.getIpv6Address()));
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
    }

}
