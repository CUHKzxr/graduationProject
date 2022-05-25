package com.example.demo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.RsiData;
import com.example.demo.entity.RsiDataWithIpv4;
import com.example.demo.entity.RsiDataWithIpv6;
import com.example.demo.mapper.RsiDataMapper;
import com.example.demo.mapper.RsiRouteDataIpv4Mapper;
import com.example.demo.mapper.RsiRouteDataIpv6Mapper;
import com.example.demo.utils.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional(propagation= Propagation.NESTED,isolation= Isolation.READ_UNCOMMITTED,readOnly = true)
@Service("ShowDataService")
public class ShowDataServiceImp implements ShowDataService {

    @Autowired
    private RsiDataMapper rsiDataMapper;
    @Autowired
    private RsiRouteDataIpv4Mapper rsiRouteDataIpv4Mapper;
    @Autowired
    private RsiRouteDataIpv6Mapper rsiRouteDataIpv6Mapper;


    @Override
    public String updateDataLocal(String requestData) {
        JSONObject requestJson = JSON.parseObject(requestData);
        int latestN = requestJson.getInteger("latestN");
        String timeStamp = requestJson.getString("timestamp");
        ArrayList<Map> data = new ArrayList<Map>();
        ArrayList<String> time = new ArrayList<String>();

        if (latestN >= 1024 || latestN < 0) {
            throw new RuntimeException("invalid latestN!");
        }
        String provider = requestJson.getString("provider");
        if (provider == null || provider.equals("")) {
            //provider = InputDataServiceImp.defaultProvider;
            provider="0";
        }
        List<RsiData>[] rsiDataLists = new List[13];
        if (timeStamp == null || timeStamp.equals("")){
            for (int i = 0; i < 13; i++) {
                char ch = (char) (65 + i);
                rsiDataLists[i] = rsiDataMapper.selectList(
                        new QueryWrapper<RsiData>()
                                //.select("identification","time_stamp","query_latency_ipv4_udp","query_latency_ipv6_udp")
                                .eq("name_", ch)
                                .eq("provider", provider)
                                .orderByDesc("timestamp_")
                                .last(" LIMIT " + latestN)
                );
            }
        }else {
            for (int i = 0; i < 13; i++) {
                char ch = (char) (65 + i);
                rsiDataLists[i] = rsiDataMapper.selectList(
                        new QueryWrapper<RsiData>()
                                //.select("identification","time_stamp","query_latency_ipv4_udp","query_latency_ipv6_udp")
                                .eq("provider", provider)
                                .eq("name_", ch)
                                .le("timestamp_", timeStamp)
                                .orderByDesc("timestamp_")
                                .last(" LIMIT " + latestN)
                );
            }
        }
        int size = Math.min(rsiDataLists[0].size(), latestN);
        for (int i = 1; i <= size; i++) {
            for (int j = 0; j < 13; j++) {
                RsiData temp = rsiDataLists[j].get(size - i);
                data.add(new HashMap<String, Object>() {{
                    put("time", temp.getTimestamp());
                    put("identification", temp.getName() + ":" + temp.getIdentification());
                    put("ipv4_latency_min", minLatency(temp.getQueryLatencyIpv4Udp(), temp.getQueryLatencyIpv4Tcp()));
                    put("ipv6_latency_min", minLatency(temp.getQueryLatencyIpv6Udp(), temp.getQueryLatencyIpv6Tcp()));
                }});
            }
            time.add(rsiDataLists[0].get(size - i).getTimestamp());
        }

        Map<String, Object> rMap = new HashMap<String, Object>();
        rMap.put("code", "200");
        rMap.put("time", time);
        rMap.put("data", data);
        String r = JSON.toJSONString(rMap);
        System.out.println(r);
        return r;
    }

    @Override
    public String searchData(String requestData) {
        JSONObject tempJson=JSON.parseObject(requestData);
        JSONObject requestJson = JSON.parseObject(tempJson.getString("data"));
        //int latestN = requestJson.getInteger("latestN");
        JSONArray timestampRange= requestJson.getJSONArray("timestamprange");
        String startTimeStamp =null;
        String endTimeStamp =null;
        if(timestampRange.size()==2){
            startTimeStamp = timestampRange.getString(0);
            endTimeStamp = timestampRange.getString(1);
        }
        List<String> providers=requestJson.getJSONArray("checkedProviders").toJavaList(String.class);
        List<Character> names=requestJson.getJSONArray("checkedNameList").toJavaList(Character.class);
        List<Map<String, Object>> dataList=searchData(startTimeStamp,endTimeStamp,providers,names);
        Map<String, Object> rMap = new HashMap<String, Object>();
        rMap.put("code", "200");
        rMap.put("data", dataList);
        String r = JSON.toJSONString(rMap);
        System.out.println(r);
        return r;
    }

    @Override
    public void test() {

        List<RsiDataWithIpv4> list = rsiDataMapper.queryDetailIpv4(new QueryWrapper<RsiData>().eq("a.name_", "A"));
        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String, Object> rMap=new HashMap<>();
        List<Integer> routeList= new ArrayList<>();

        String timestampPre = "!!!";
        String providerPre = "!!!";
        char namePre = ' ';
        String identificationPre = "!!!";
        int pathCountIpv4Pre = 0;

        for (RsiDataWithIpv4 data : list) {
            String timestamp = data.getTimestamp();
            String provider = data.getProvider();
            char name = data.getName();
            String identification = data.getIdentification();
            if (timestamp.equals(timestampPre) && provider.equals(providerPre) && name == namePre && identification.equals(identificationPre)) {
                routeList.add(data.getIpv4RouteAddress());
            } else {
                if(routeList.size()!=pathCountIpv4Pre){
                    throw new RuntimeException("route address size want wrong!");
                }

                timestampPre=timestamp;
                providerPre=provider;
                namePre=name;
                identificationPre=identification;
                pathCountIpv4Pre=data.getPathCountIpv4();
                routeList= new ArrayList<Integer>(){{
                    add(data.getIpv4RouteAddress());
                }};
                List<Integer> finalRouteList = routeList;
                rMap=new HashMap<String,Object>(){{
                    put("timestamp",timestamp);
                    put("provider",provider);
                    put("name",name);
                    put("identification",identification);
                    put("query_latency_ipv4_udp",data.getQueryLatencyIpv4Udp());
                    put("query_latency_ipv4_tcp",data.getQueryLatencyIpv4Tcp());
                    put("query_latency_ipv6_udp",data.getQueryLatencyIpv6Udp());
                    put("query_latency_ipv6_tcp",data.getQueryLatencyIpv6Tcp());
                    put("status",data.getStatus());
                    put("source_ip_ipv4",data.getSourceIpIpv4());
                    put("source_ip_ipv6",data.getSourceIpIpv6());
                    put("refer_latency_ipv4_0",data.getReferLatencyIpv4_0());
                    put("refer_latency_ipv4_1",data.getReferLatencyIpv4_1());
                    put("refer_latency_ipv4_2",data.getReferLatencyIpv4_2());
                    put("refer_latency_ipv6_0",data.getReferLatencyIpv6_0());
                    put("refer_latency_ipv6_1",data.getReferLatencyIpv6_1());
                    put("refer_latency_ipv6_2",data.getReferLatencyIpv6_2());
                    put("publication_latency",data.getPublicationLatency());
                    put("correctness",data.Correctness());
                    put("path_ipv4", finalRouteList);
                }};
                resultList.add(rMap);
            }
        }
        String jsonString=JSON.toJSONString(new HashMap<String,Object>(){{
            put("code",200);
            put("data",resultList);
        }});
        System.out.println(jsonString);

    }

    public List<Map<String, Object>> searchData(String startTimeStamp, String endTimeStamp , List<String> providers ,List<Character> names) {
        try {
            List<Map<String, Object>> resultList = new ArrayList<>();
            List<Integer> routeList= new ArrayList<>();
            if (providers.size()==0) {
                providers.add(InputDataServiceImp.defaultProvider);
            }

            // TODO: 考虑把遍历provider改到条件构造器里
            List<RsiDataWithIpv4> rsiDataListIpv4 = new ArrayList<>();
            List<RsiDataWithIpv6> rsiDataListIpv6 = new ArrayList<>();
            if (startTimeStamp!=null && endTimeStamp!=null) {
                for(String provider :providers){
                    for(Character name:names){
                        rsiDataListIpv4.addAll(rsiDataMapper.queryDetailIpv4(new QueryWrapper<RsiData>()
                                .eq("a.provider", provider)
                                .eq("a.name_", name)
                                .ge("a.timestamp_", startTimeStamp)
                                .le("a.timestamp_",endTimeStamp)
                        ));
                    }
                }
                for(String provider :providers){
                    for(Character name:names){
                        rsiDataListIpv6.addAll(rsiDataMapper.queryDetailIpv6(new QueryWrapper<RsiData>()
                                .eq("a.provider", provider)
                                .eq("a.name_", name)
                                .ge("a.timestamp_", startTimeStamp)
                                .le("a.timestamp_",endTimeStamp)
                        ));
                    }
                }
            } else if (startTimeStamp==null && endTimeStamp!=null) {
                for(String provider :providers){
                    for(Character name:names){
                        rsiDataListIpv4.addAll(rsiDataMapper.queryDetailIpv4(new QueryWrapper<RsiData>()
                                .eq("a.provider", provider)
                                .eq("a.name_", name)
                                .le("a.timestamp_", endTimeStamp)
                        ));
                    }
                }
                for(String provider :providers){
                    for(Character name:names){
                        rsiDataListIpv6.addAll(rsiDataMapper.queryDetailIpv6(new QueryWrapper<RsiData>()
                                .eq("a.provider", provider)
                                .eq("a.name_", name)
                                .le("a.timestamp_", endTimeStamp)
                        ));
                    }
                }
            } else if (startTimeStamp!=null) {
                for(String provider :providers){
                    for(Character name:names){
                        rsiDataListIpv4.addAll(rsiDataMapper.queryDetailIpv4(new QueryWrapper<RsiData>()
                                .eq("a.provider", provider)
                                .eq("a.name_", name)
                                .ge("a.timestamp_", endTimeStamp)
                        ));
                    }
                }
                for(String provider :providers){
                    for(Character name:names){
                        rsiDataListIpv6.addAll(rsiDataMapper.queryDetailIpv6(new QueryWrapper<RsiData>()
                                .eq("a.provider", provider)
                                .eq("a.name_", name)
                                .ge("a.timestamp_", endTimeStamp)
                        ));
                    }
                }
            } else {
                for(String provider :providers){
                    for(Character name:names){
                        rsiDataListIpv4.addAll(rsiDataMapper.queryDetailIpv4(new QueryWrapper<RsiData>()
                                .eq("a.provider", provider)
                                .eq("a.name_", name)
                        ));
                    }
                }
                for(String provider :providers){
                    for(Character name:names){
                        rsiDataListIpv6.addAll(rsiDataMapper.queryDetailIpv6(new QueryWrapper<RsiData>()
                                .eq("a.provider", provider)
                                .eq("a.name_", name)
                        ));
                    }
                }
            }
            String timestampPre = null;
            String providerPre = null;
            char namePre = ' ';
            String identificationPre = null;
            int pathCountIpv4Pre = 0;
            Map<String,Object> rMap;
            for (RsiDataWithIpv4 data : rsiDataListIpv4) {
                String timestamp = data.getTimestamp();
                String tempProvider = data.getProvider();
                char name = data.getName();
                String identification = data.getIdentification();
                if (timestamp.equals(timestampPre) && tempProvider.equals(providerPre) && name == namePre && identification.equals(identificationPre)) {
                    routeList.add(data.getIpv4RouteAddress());
                } else {
                    if(routeList.size()!=pathCountIpv4Pre && pathCountIpv4Pre!=-1){
                        throw new RuntimeException("route address size want wrong!");
                    }

                    timestampPre=timestamp;
                    providerPre=tempProvider;
                    namePre=name;
                    identificationPre=identification;
                    pathCountIpv4Pre=data.getPathCountIpv4();
                    routeList= new ArrayList<Integer>(){{
                        add(data.getIpv4RouteAddress());
                    }};
                    List<Integer> finalRouteList = routeList;
                    rMap=new HashMap<String,Object>(){{
                        put("timestamp",timestamp);
                        put("provider",tempProvider);
                        put("name",name);
                        put("identification",identification);
                        put("query_latency_ipv4_udp",data.getQueryLatencyIpv4Udp());
                        put("query_latency_ipv4_tcp",data.getQueryLatencyIpv4Tcp());
                        put("query_latency_ipv6_udp",data.getQueryLatencyIpv6Udp());
                        put("query_latency_ipv6_tcp",data.getQueryLatencyIpv6Tcp());
                        put("status",data.getStatus());
                        put("source_ip_ipv4",data.getSourceIpIpv4());
                        put("source_ip_ipv6", IpUtils.ipv6ToString(data.getSourceIpIpv6()));
                        put("refer_latency_ipv4_0",data.getReferLatencyIpv4_0());
                        put("refer_latency_ipv4_1",data.getReferLatencyIpv4_1());
                        put("refer_latency_ipv4_2",data.getReferLatencyIpv4_2());
                        put("refer_latency_ipv6_0",data.getReferLatencyIpv6_0());
                        put("refer_latency_ipv6_1",data.getReferLatencyIpv6_1());
                        put("refer_latency_ipv6_2",data.getReferLatencyIpv6_2());
                        put("publication_latency",data.getPublicationLatency());
                        put("correctness",data.Correctness());
                        put("path_ipv4", finalRouteList);
                    }};
                    resultList.add(rMap);
                }
            }
            timestampPre = null;
            providerPre = null;
            namePre = ' ';
            identificationPre = null;
            int pathCountIpv6Pre = 0;
            List<String> routeListIpv6=new ArrayList<>();
            int i=0;
            int size=resultList.size();
            for(RsiDataWithIpv6 data : rsiDataListIpv6){
                String timestamp = data.getTimestamp();
                String tempProvider = data.getProvider();
                char name = data.getName();
                String identification = data.getIdentification();
                if (timestamp.equals(timestampPre) && tempProvider.equals(providerPre) && name == namePre && identification.equals(identificationPre)) {
                    routeListIpv6.add(IpUtils.ipv6ToString(data.getIpv6RouteAddress()));
                } else {
                    if (routeListIpv6.size() != pathCountIpv6Pre && pathCountIpv6Pre != -1) {
                        throw new RuntimeException("route address size want wrong!");
                    }

                    timestampPre = timestamp;
                    providerPre = tempProvider;
                    namePre = name;
                    identificationPre = identification;
                    pathCountIpv6Pre = data.getPathCountIpv6();
                    routeListIpv6 = new ArrayList<String>() {{
                        add(IpUtils.ipv6ToString(data.getIpv6RouteAddress()));
                    }};
                    while (!primaryKeysIsEqual(resultList.get(i),data)){
                        i++;
                    }
                    resultList.get(i).put("path_ipv6",routeListIpv6);
                }

            }

            return resultList;
        }catch (UnknownHostException e){
            e.printStackTrace();
            return null;
        }
    }


    private static int minLatency(int latency1, int latency2) {
        if (latency1 == -1) {
            return latency2;
        } else if (latency1 <= latency2) {
            return latency1;
        } else if (latency2 == -1) {
            return latency1;
        } else return latency2;
    }

    private static boolean primaryKeysIsEqual(RsiDataWithIpv4 ipv4,RsiDataWithIpv6 ipv6){
        if(ipv4.getTimestamp().equals(ipv6.getTimestamp())
                &&ipv4.getProvider().equals(ipv6.getProvider())
                &&ipv4.getName()==ipv6.getName()
                &&ipv4.getIdentification().equals(ipv6.getIdentification())
        ){
            return true;
        }else {
            return false;
        }
    }
    private static boolean primaryKeysIsEqual(String ipv4TimeStamp,String ipv4Provider,char ipv4Name,String ipv4Identification,RsiDataWithIpv6 ipv6){
        if(ipv4TimeStamp.equals(ipv6.getTimestamp())
                &&ipv4Provider.equals(ipv6.getProvider())
                &&ipv4Name==ipv6.getName()
                &&ipv4Identification.equals(ipv6.getIdentification())
        ){
            return true;
        }else {
            return false;
        }
    }
    private static boolean primaryKeysIsEqual(Map<String,Object> ipv4Map,RsiDataWithIpv6 ipv6){
        if(ipv4Map.get("timestamp").equals(ipv6.getTimestamp())
                &&ipv4Map.get("provider").equals(ipv6.getProvider())
                &&ipv4Map.get("name").equals(ipv6.getName())
                &&ipv4Map.get("identification").equals(ipv6.getIdentification())
        ){
            return true;
        }else {
            return false;
        }
    }

}
