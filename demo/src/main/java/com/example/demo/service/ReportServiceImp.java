package com.example.demo.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.RsiData;
import com.example.demo.entity.RsiReport;
import com.example.demo.entity.RssReport;
import com.example.demo.mapper.RsiDataMapper;
import com.example.demo.mapper.RsiReportMapper;
import com.example.demo.mapper.RssReportMapper;
import com.example.demo.report.BooleanResult;
import com.example.demo.report.Report;
import com.example.demo.report.Result;
import com.example.demo.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("ReportService")
public class ReportServiceImp implements ReportService {
    private final Logger logger= LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RsiReportMapper rsiReportMapper;
    @Autowired
    private RssReportMapper rssReportMapper;
    @Autowired
    private RsiDataMapper rsiDataMapper;

    @Override
    public String getExistedReport(String timeString) {
        String time = timeString.substring(0, 10);
        List<RsiReport> rsiReports = rsiReportMapper.selectList(new QueryWrapper<RsiReport>()
                .eq("timestamp_ymd", time)
                .orderByAsc("name_")
        );
        RssReport rssReport = rssReportMapper.selectById(time);
        Map<String,Object> rMap=new HashMap<>();
        rMap.put("rsi",rsiResultWrapper(rsiReports));
        rMap.put("rss",rssReport);
        return JSON.toJSONString(rMap);
    }

    @Override
    public String getExistedReport(String startTimeString, String endTimeString) {
        String startTime = startTimeString.substring(0, 10);
        String endTime=endTimeString.substring(0,10);
        List<RsiReport> rsiReports = rsiReportMapper.selectList(new QueryWrapper<RsiReport>()
                .ge("timestamp_ymd",startTime)
                .le("timestamp_ymd",endTime)
                .orderByAsc("timestamp_ymd","name_")
        );
        List<RssReport> rssReport=rssReportMapper.selectList(new QueryWrapper<RssReport>()
                .ge("timestamp_ymd",startTime)
                .le("timestamp_ymd",endTime)
                .orderByAsc("timestamp_ymd")
        );
        Map<String,Object> rMap=new HashMap<>();
        rMap.put("rsi",rsiResultWrapper(rsiReports));
        rMap.put("rss",rssReport);
        return JSON.toJSONString(rMap);
    }


    @Override
    public String getNewReport(String startTimestamp,String endTimestamp) {
        if(!TimeUtils.checkTimestamp(startTimestamp,5) || !TimeUtils.checkTimestamp(endTimestamp,5)){
            throw new IllegalArgumentException("ReportService.getNewReport:timestamp string went wrong");
        }
        List<RsiData> rsiDataList=rsiDataMapper.selectList(new QueryWrapper<RsiData>()
                .between("timestamp_",startTimestamp,endTimestamp)
                .orderByAsc("timestamp_","provider","name_")
        );
        Map<String,Object> rssReport=Report.calRssReport(rsiDataList);
        List<Map<String,Object>> rsiReports=new ArrayList<>();
        List<RsiData>[] lists=new List[13];
        for (int i = 0; i < 13; i++) {
            lists[i]=new ArrayList<RsiData>();
        }
        for(RsiData data:rsiDataList){
            lists[data.getName()-65].add(data);
        }
        for (int i = 0; i < 13; i++) {
            rsiReports.add(Report.calRsiReportBoolean(lists[i],(char)(i+65)));
        }
        Map<String,Object> rMap=new HashMap<String,Object>(){{
            put("rsi",rsiReports);
            put("rss",rssReport);
        }};
        return JSON.toJSONString(rMap);
    }

    /**
     * 生成日度报告
     * @param dateString 日期字符串，指定具体日期，如2022-01-01
     * @return 0 如果选定的日期内数据不为空，报告正常生成；-1 出错;0 指定日期内数据为空
     */
    @Override
    public int generateDailyReport(String dateString){
        if(!TimeUtils.checkTimestamp(dateString,2)){
            throw new IllegalArgumentException("ReportService.generateDailyReport:timestamp string went wrong");
        }
        List<RsiData> rsiDataList=rsiDataMapper.selectList(new QueryWrapper<RsiData>()
                .ge("timestamp_",dateString+"T00:00:00Z")
                .le("timestamp_",dateString+"T24:00:00Z")
                .orderByAsc("timestamp_","provider","name_")
        );
        if(rsiDataList.size()==0){
            logger.warn("empty dataset. date:"+dateString);
            return 0;
        }
        Map rssReport=Report.calRssReport(rsiDataList);
        if(!saveRssReportMap(dateString, rssReport)){
            logger.error("save rssReport went wrong!date:"+dateString);
            return -1;
        }
        List<RsiData>[] lists=new List[13];
        for (int i = 0; i < 13; i++) {
            lists[i]=new ArrayList<RsiData>();
        }
        for(RsiData data:rsiDataList){
            lists[data.getName()-65].add(data);
        }
        for (int i = 0; i < 13; i++) {
            char name=(char)(i+65);
           Map map=Report.calRsiReport(lists[i],name);
           if(!saveRsiReportMap(dateString,name+"",map)){
               logger.error("save rssReport went wrong!date:"+dateString);
               return -1;
           }
        }
        logger.info("Generate and save daily report:"+dateString);
        return 1;
    }

    @Override
    public int generateHourlyReport(String dateString) {
        if(!TimeUtils.checkTimestamp(dateString,3)){
            throw new IllegalArgumentException("ReportService.generateHourlyReport:timestamp string went wrong!,str:"+dateString);
        }
        List<RsiData> rsiDataList=rsiDataMapper.selectList(new QueryWrapper<RsiData>()
                .ge("timestamp_",dateString+":00:00Z")
                .le("timestamp_",dateString+":60:00Z")
                .orderByAsc("timestamp_","provider","name_")
        );
        if(rsiDataList.size()==0){
            logger.warn("empty dataset. date:"+dateString);
            return 0;
        }
        Map rssReport=Report.calRssReport(rsiDataList);
        if(!saveRssReportMap(dateString, rssReport)){
            logger.error("save rssReport went wrong!date:"+dateString);
            return -1;
        }
        List<RsiData>[] lists=new List[13];
        for (int i = 0; i < 13; i++) {
            lists[i]=new ArrayList<RsiData>();
        }
        for(RsiData data:rsiDataList){
            lists[data.getName()-65].add(data);
        }
        for (int i = 0; i < 13; i++) {
            char name=(char)(i+65);
            Map map=Report.calRsiReport(lists[i],name);
            if(!saveRsiReportMap(dateString,name+"",map)){
                logger.error("save rssReport went wrong!date:"+dateString);
                return -1;
            }
        }
        logger.info("Generate and save hourly report:"+dateString);
        return 1;
    }

    private boolean saveRsiReportMap(String time,String name,Map<String,Result> map){
        Result ipv4_udp_availability=map.get("ipv4_udp_availability");
        Result ipv4_tcp_availability=map.get("ipv4_tcp_availability");
        Result ipv6_udp_availability=map.get("ipv6_udp_availability");
        Result ipv6_tcp_availability=map.get("ipv6_tcp_availability");
        Result ipv4_udp_latency=map.get("ipv4_udp_latency");
        Result ipv4_tcp_latency=map.get("ipv4_tcp_latency");
        Result ipv6_udp_latency=map.get("ipv6_udp_latency");
        Result ipv6_tcp_latency=map.get("ipv6_tcp_latency");
        Result correctness=map.get("correctness");
        Result publication_latency=map.get("publication_latency");
        RsiReport rsiReport=new RsiReport(time,name,ipv4_udp_availability.getValue(),ipv4_tcp_availability.getValue()
                ,ipv6_udp_availability.getValue(),ipv6_tcp_availability.getValue()
                ,(int)ipv4_udp_latency.getValue(),(int)ipv4_tcp_latency.getValue()
                ,(int)ipv6_udp_latency.getValue(),(int)ipv6_tcp_latency.getValue()
                ,correctness.getValue(),(int)publication_latency.getValue()
                ,ipv4_udp_availability.getMeasurementsSize(),ipv4_tcp_availability.getMeasurementsSize()
                ,ipv6_udp_availability.getMeasurementsSize(),ipv6_tcp_availability.getMeasurementsSize()
                ,ipv4_udp_latency.getMeasurementsSize(),ipv4_tcp_latency.getMeasurementsSize()
                ,ipv6_udp_latency.getMeasurementsSize(),ipv6_tcp_latency.getMeasurementsSize()
                ,correctness.getMeasurementsSize(), publication_latency.getMeasurementsSize()
                );
        return rsiReportMapper.insert(rsiReport) == 1;
    }

    private boolean saveRssReportMap(String time ,Map<String,Result> map){
        Result ipv4_udp_availability=map.get("ipv4_udp_availability");
        Result ipv4_tcp_availability=map.get("ipv4_tcp_availability");
        Result ipv6_udp_availability=map.get("ipv6_udp_availability");
        Result ipv6_tcp_availability=map.get("ipv6_tcp_availability");
        Result ipv4_udp_latency=map.get("ipv4_udp_latency");
        Result ipv4_tcp_latency=map.get("ipv4_tcp_latency");
        Result ipv6_udp_latency=map.get("ipv6_udp_latency");
        Result ipv6_tcp_latency=map.get("ipv6_tcp_latency");
        Result correctness=map.get("correctness");
        Result publication_latency=map.get("publication_latency");
        RssReport rssReport=new RssReport(time,ipv4_udp_availability.getValue(),ipv4_tcp_availability.getValue()
                ,ipv6_udp_availability.getValue(),ipv6_tcp_availability.getValue()
                ,(int)ipv4_udp_latency.getValue(),(int)ipv4_tcp_latency.getValue()
                ,(int)ipv6_udp_latency.getValue(),(int)ipv6_tcp_latency.getValue()
                ,correctness.getValue(),(int)publication_latency.getValue()
                ,ipv4_udp_availability.getMeasurementsSize(),ipv4_tcp_availability.getMeasurementsSize()
                ,ipv6_udp_availability.getMeasurementsSize(),ipv6_tcp_availability.getMeasurementsSize()
                ,ipv4_udp_latency.getMeasurementsSize(),ipv4_tcp_latency.getMeasurementsSize()
                ,ipv6_udp_latency.getMeasurementsSize(),ipv6_tcp_latency.getMeasurementsSize()
                ,correctness.getMeasurementsSize(), publication_latency.getMeasurementsSize());
        return rssReportMapper.insert(rssReport)==1;
    }

    private static List<Map<String, Object>> rsiResultWrapper(List<RsiReport> reportResult) {
        List<Map<String, Object>> rList = new ArrayList<>();
        for (RsiReport data : reportResult) {
            Map<String, Object> tempMap = new HashMap<String, Object>() {{
                put("time", data.getTimestamp());
                put("name", data.getName());
                put("ipv4_udp_availability", new BooleanResult(data.getAvailabilityIpv4Udp() >= 0.96,
                        data.getIpv4UdpAvailabilityMeasurements()));
                put("ipv4_tcp_availability", new BooleanResult(data.getAvailabilityIpv4Tcp() >= 0.96,
                        data.getIpv4TcpAvailabilityMeasurements()));
                put("ipv6_udp_availability", new BooleanResult(data.getAvailabilityIpv6Udp() >= 0.96,
                        data.getIpv6UdpAvailabilityMeasurements()));
                put("ipv6_tcp_availability", new BooleanResult(data.getAvailabilityIpv6Tcp() >= 0.96,
                        data.getIpv6TcpAvailabilityMeasurements()));
                put("ipv4_udp_latency", new BooleanResult(data.getLatencyIpv4Udp() <= 250,
                        data.getIpv4UdpLatencyMeasurements()));
                put("ipv4_tcp_latency", new BooleanResult(data.getLatencyIpv4Tcp() <= 500,
                        data.getIpv4TcpLatencyMeasurements()));
                put("ipv6_udp_latency", new BooleanResult(data.getLatencyIpv6Udp() <= 250,
                        data.getIpv6UdpLatencyMeasurements()));
                put("ipv6_tcp_latency", new BooleanResult(data.getLatencyIpv6Tcp() <= 500,
                        data.getIpv6TcpLatencyMeasurements()));
                put("correctness", new BooleanResult(Report.checkCorrectness(data.getCorrectness()),
                        data.getCorrectnessMeasurements()));
                put("publication_latency",new BooleanResult(data.getPublicationLatency()<=3900000,
                        data.getPublicationLatencyMeasurements()));
            }};
            rList.add(tempMap);
        }
        return rList;
    }

    private static Map<String, Object> rssResultWrapper(RssReport reportResult) {
        Map<String, Object> tempMap = new HashMap<String, Object>() {{
            put("time", reportResult.getTimestamp());
            put("ipv4_udp_availability", new BooleanResult(reportResult.getAvailabilityIpv4Udp() >= 0.99999,
                    reportResult.getIpv4UdpAvailabilityMeasurements()));
            put("ipv4_tcp_availability", new BooleanResult(reportResult.getAvailabilityIpv4Tcp() >= 0.99999,
                    reportResult.getIpv4TcpAvailabilityMeasurements()));
            put("ipv6_udp_availability", new BooleanResult(reportResult.getAvailabilityIpv6Udp() >= 0.99999,
                    reportResult.getIpv6UdpAvailabilityMeasurements()));
            put("ipv6_tcp_availability", new BooleanResult(reportResult.getAvailabilityIpv6Tcp() >= 0.99999,
                    reportResult.getIpv6TcpAvailabilityMeasurements()));
            put("ipv4_udp_latency", new BooleanResult(reportResult.getLatencyIpv4Udp() <= 150,
                    reportResult.getIpv4UdpLatencyMeasurements()));
            put("ipv4_tcp_latency", new BooleanResult(reportResult.getLatencyIpv4Tcp() <= 300,
                    reportResult.getIpv4TcpLatencyMeasurements()));
            put("ipv6_udp_latency", new BooleanResult(reportResult.getLatencyIpv6Udp() <= 150,
                    reportResult.getIpv6UdpLatencyMeasurements()));
            put("ipv6_tcp_latency", new BooleanResult(reportResult.getLatencyIpv6Tcp() <= 300,
                    reportResult.getIpv6TcpLatencyMeasurements()));
            put("correctness", new BooleanResult(reportResult.getCorrectness()==1,reportResult.getCorrectnessMeasurements()));
            put("publication_latency",new BooleanResult(reportResult.getPublicationLatency()<=2100000,
                    reportResult.getPublicationLatencyMeasurements()));
        }};

        return tempMap;
    }
}
