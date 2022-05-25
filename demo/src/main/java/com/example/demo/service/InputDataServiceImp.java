package com.example.demo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.MeasuringPoint;
import com.example.demo.entity.RsiData;
import com.example.demo.entity.RsiRouteDataIpv4;
import com.example.demo.entity.RsiRouteDataIpv6;
import com.example.demo.mapper.MeasuringPointsMapper;
import com.example.demo.mapper.RsiDataMapper;
import com.example.demo.mapper.RsiRouteDataIpv4Mapper;
import com.example.demo.mapper.RsiRouteDataIpv6Mapper;
import com.example.demo.utils.IpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;



@Service("InputDataService")
public class InputDataServiceImp implements InputDataService {

    public static final String defaultProvider = "local";
    private static final int maxJumps = 32;
    private final Logger logger= LoggerFactory.getLogger(this.getClass());


    @Autowired
    private MeasuringPointsMapper measuringPointsMapper;

    @Autowired
    private RsiDataMapper rsiDataMapper;
    @Autowired
    private RsiRouteDataIpv4Mapper rsiRouteDataIpv4Mapper;
    @Autowired
    private RsiRouteDataIpv6Mapper rsiRouteDataIpv6Mapper;

    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.READ_UNCOMMITTED, readOnly = false)
    @Override
    public int saveData(String dataString) {
        if(dataString==null || dataString.equals("") || dataString.length()<=1){
            logger.warn("Empty FIle!");
            return 0;
        }
        JSONObject rsiData13 = JSON.parseObject(dataString);
        String timestamp = rsiData13.getString("TimeStamp");
        JSONObject rootA=rsiData13.getJSONObject("A");
        String ipv4=rootA.getString("SourceIP_ipv4");
        String ipv6=rootA.getString("SourceIP_ipv6");
        String provider=checkProvider(rsiData13);
        if(provider==null){
            logger.warn("Unknown Provider:Timestamp:"+timestamp+"  ipv4:"+ipv4+"  ipv6:"+ipv6);
            return 0;
        }
        int r=0;
        for (int i = 0; i < 13; i++) {
            String name = "" + (char) ('A' + i);
            JSONObject rsiData = rsiData13.getJSONObject(name);
            r+= saveRsiData(rsiData, timestamp, provider, (char) ('A' + i));
        }
        if(r!=13){
            logger.error("Input data error!Should get 13 Rsi but not "+r+".  "+
                    "Provider:"+provider+" Timestamp:"+timestamp);
        }
        return r;
    }


    private int saveRsiData(JSONObject jsonObject, String timestamp, String provider, char name) {
        //检查数据
        /*
        if (!checkRsiData(jsonObject)) {
            return -1;
        }
        */
        try {
            int r = 0;

            String identification = jsonObject.getString("Identification");
            int[] latency = new int[4];
            JSONObject queryLatencyJson = jsonObject.getJSONObject("QueryLatency");
            latency[0] = toInt(queryLatencyJson.getString("Ipv4_udp"));
            latency[1] = toInt(queryLatencyJson.getString("Ipv4_tcp"));
            latency[2] = toInt(queryLatencyJson.getString("Ipv6_udp"));
            latency[3] = toInt(queryLatencyJson.getString("Ipv6_tcp"));

            int[] pathCount = new int[2];
            pathCount[0] = toInt(jsonObject.getString("Path_count_ipv4"));
            pathCount[1] = toInt(jsonObject.getString("Path_count_ipv6"));
            int[] referLatency = new int[6];
            JSONObject referLatencyIpv4Json = jsonObject.getJSONObject("ReferLatency_ipv4");
            JSONObject referLatencyIpv6Json = jsonObject.getJSONObject("ReferLatency_ipv6");
            referLatency[0] = toInt(jsonObject.getJSONObject("ReferLatency_ipv4").getString("AliDNS"));
            referLatency[1] = toInt(jsonObject.getJSONObject("ReferLatency_ipv4").getString("114DNS"));
            referLatency[2] = toInt(jsonObject.getJSONObject("ReferLatency_ipv4").getString("DNSPod"));
            referLatency[3] = toInt(jsonObject.getJSONObject("ReferLatency_ipv6").getString("AliDNS"));
            referLatency[4] = toInt(jsonObject.getJSONObject("ReferLatency_ipv6").getString("114DNS"));
            referLatency[5] = toInt(jsonObject.getJSONObject("ReferLatency_ipv6").getString("DNSPod"));

            String status = jsonObject.getString("Status");
            int sourceIpv4 = IpUtils.parseIntIpv4(jsonObject.getString("SourceIP_ipv4"));
            byte[] sourceIpv6 = IpUtils.parseIpv6(jsonObject.getString("SourceIP_ipv6"));
            int publicationLatency = toInt(jsonObject.getString("Publication_latency"));
            boolean correctness = jsonObject.getString("Correctness").equals("1") ? true : false;


            RsiData rsiData = new RsiData(timestamp, provider, name, identification, latency,
                    pathCount, status, sourceIpv4, sourceIpv6, referLatency, correctness, publicationLatency);
            r+=rsiDataMapper.insert(rsiData);

            //List<RsiRouteDataIpv4> rsiRouteDataIpv4List;
            //List<RsiRouteDataIpv6> rsiRouteDataIpv6List;
            JSONObject pathIpv4Json = jsonObject.getJSONObject("Path_ipv4");
            JSONObject pathIpv6Json = jsonObject.getJSONObject("Path_ipv6");
            if (pathIpv4Json == null || jsonObject.getIntValue("Path_count_ipv4")==-1) {
                //rsiRouteDataIpv4List = null;
                //rsiRouteDataIpv4Mapper.insert(new RsiRouteDataIpv4(timestamp, provider, name, identification,0, 0));
            } else {
                //rsiRouteDataIpv4List = new ArrayList<RsiRouteDataIpv4>();
                int i = 0;
                String address = pathIpv4Json.getString("Router" + i);
                while (i < maxJumps && address != null) {
                    RsiRouteDataIpv4 temp = new RsiRouteDataIpv4(timestamp, provider, name, identification, i, IpUtils.parseIntIpv4(address));
                    //rsiRouteDataIpv4List.add(temp);
                    rsiRouteDataIpv4Mapper.insert(temp);
                    i++;
                    address = pathIpv4Json.getString("Router" + i);
                }
            }
            if (pathIpv6Json == null|| jsonObject.getIntValue("Path_count_ipv6")==-1) {
                //rsiRouteDataIpv6List = null;
                //rsiRouteDataIpv6Mapper.insert(new RsiRouteDataIpv6(timestamp, provider, name, identification,0, null));
            } else {
                //rsiRouteDataIpv6List = new ArrayList<RsiRouteDataIpv6>();
                int i = 0;
                String address = pathIpv6Json.getString("Router" + i);
                while (i < maxJumps && address != null) {
                    RsiRouteDataIpv6 temp = new RsiRouteDataIpv6(timestamp, provider, name, identification, i, IpUtils.parseIpv6(address));
                    //rsiRouteDataIpv6List.add(temp);
                    rsiRouteDataIpv6Mapper.insert(temp);
                    i++;
                    address = pathIpv6Json.getString("Router" + i);
                }
            }

            /*
            TODO ，完善返回值
             */

            return r;
        }catch (UnknownHostException e){
            e.printStackTrace();
            return -1;
        }
    }

    private static int toInt(String a) {
        if (a.equals("") || (a.equals(null)) || a.equals(" ")) {
            return -1;
        } else {
            return Integer.parseInt(a);
        }
    }
    /**
     * 检查单条rsi数据的提供者
     *
     * @param rsiData13
     * @return true如果合格，false如果出错
     */
    public String checkProvider(JSONObject rsiData13){
        try {
            JSONObject rootA=rsiData13.getJSONObject("A");
            int ipv4=IpUtils.parseIntIpv4(rootA.getString("SourceIP_ipv4"));
            byte[] ipv6=IpUtils.parseIpv6(rootA.getString("SourceIP_ipv6"));
            MeasuringPoint measuringPoint=measuringPointsMapper.selectOne(new QueryWrapper<MeasuringPoint>()
                    .eq("ipv4_address",ipv4)
                    //.eq("ipv6_address",ipv6)
            );
            if(measuringPoint!=null){
                return measuringPoint.getIdentification();
            }
            return null;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }

    }


    /*
    private boolean checkRsiData(JSONObject json) {
        //todo
        return true;
    }
*/

}
