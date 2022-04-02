package com.example.demo.utils.report;

import com.example.demo.bean.RsiData;
import org.apache.kafka.common.protocol.types.Field;

import java.lang.reflect.Array;
import java.util.*;

public class ReportUtils {
    private static final int numberK = 8;
    private static final int INT_MAX=0x7fffffff;

    public Map<String,BooleanResult>
    /**
     *
     * @param dataList 应当仅包含同一根类型的数据
     * @return
     */
    public Map<String,Object> calRsiReport(List<RsiData> dataList){
        int availabilityIpv4Udp=0;
        int availabilityIpv4Tcp=0;
        int availabilityIpv6Udp=0;
        int availabilityIpv6Tcp=0;
        int correctness=0;
        int size=dataList.size();
        int[] ipv4UdpLatency=new int[size];
        int[] ipv4TcpLatency=new int[size];
        int[] ipv6UdpLatency=new int[size];
        int[] ipv6TcpLatency=new int[size];
        int[] publicationLatency=new int[size];
        for (int i = 0; i < size; i++) {
            RsiData data=dataList.get(i);
            ipv4UdpLatency[i]=data.getQueryLatencyIpv4Udp();
            ipv4TcpLatency[i]=data.getQueryLatencyIpv4Tcp();
            ipv6UdpLatency[i]=data.getQueryLatencyIpv6Udp();
            ipv6TcpLatency[i]=data.getQueryLatencyIpv6Tcp();
            publicationLatency[i]=data.getPublicationLatency();
            if(data.isCorrectness()){
                correctness++;
                if(data.getQueryLatencyIpv4Udp()>=0){
                    availabilityIpv4Udp++;
                }
                if(data.getQueryLatencyIpv4Tcp()>=0){
                    availabilityIpv4Tcp++;
                }
                if(data.getQueryLatencyIpv6Udp()>=0){
                    availabilityIpv6Udp++;
                }
                if(data.getQueryLatencyIpv6Tcp()>=0){
                    availabilityIpv6Tcp++;
                }
            }
        }
        Map<String,Object> rMap=new HashMap<>();
        rMap.put("availability_ipv4_udp",new PercentageResult((float)availabilityIpv4Udp/size,size));
        rMap.put("availability_ipv4_tcp",new PercentageResult((float)availabilityIpv4Tcp/size,size));
        rMap.put("availability_ipv6_udp",new PercentageResult((float)availabilityIpv6Udp/size,size));
        rMap.put("availability_ipv6_tcp",new PercentageResult((float)availabilityIpv6Tcp/size,size));
        rMap.put("response_latency_ipv4_udp",new TimeResult(getMedian(ipv4UdpLatency,size),size));
        rMap.put("response_latency_ipv4_tcp",new TimeResult(getMedian(ipv4TcpLatency,size),size));
        rMap.put("response_latency_ipv6_udp",new TimeResult(getMedian(ipv6UdpLatency,size),size));
        rMap.put("response_latency_ipv6_tcp",new TimeResult(getMedian(ipv6TcpLatency,size),size));
        rMap.put("correctness",new PercentageResult((float)correctness/size,size));
        rMap.put("publication_latency",new TimeResult(getMedian(publicationLatency,size),size));
        return  rMap;
    }

    /**
     * 传入的数据应当已经按照时间、节点、类型的顺序排序
     * @param dataList
     * @return
     */
    public Map<String,Object> calRssReport(List<RsiData> dataList){
        int availabilityIpv4Udp=0;
        int availabilityIpv4Tcp=0;
        int availabilityIpv6Udp=0;
        int availabilityIpv6Tcp=0;
        int correctness=0;
        int dataSize=dataList.size();
        int[] ipv4UdpLatency=new int[dataSize];
        int[] ipv4TcpLatency=new int[dataSize];
        int[] ipv6UdpLatency=new int[dataSize];
        int[] ipv6TcpLatency=new int[dataSize];
        int latencyPointer=0;
        int publicationLatencyPointer=0;
        int[] publicationLatency=new int[dataSize];
        Iterator<RsiData> iterator=dataList.listIterator();
        String timeStamp=null;
        String provider=null;
        do{
            RsiData data=iterator.next();
            timeStamp=data.getTimestamp();
            do{
                provider=data.getProvider();
                int providerAvailabilityIpv4Udp=0;
                int providerAvailabilityIpv4Tcp=0;
                int providerAvailabilityIpv6Udp=0;
                int providerAvailabilityIpv6Tcp=0;
                List<Integer> ipv4Udp=new ArrayList<>();
                List<Integer> ipv4Tcp=new ArrayList<>();
                List<Integer> ipv6Udp=new ArrayList<>();
                List<Integer> ipv6Tcp=new ArrayList<>();
                do{
                    if(data.isCorrectness()){
                        correctness++;
                        if(data.getQueryLatencyIpv4Udp()>=0){
                            providerAvailabilityIpv4Udp++;
                        }
                        if(data.getQueryLatencyIpv4Tcp()>=0){
                            providerAvailabilityIpv4Tcp++;
                        }
                        if(data.getQueryLatencyIpv6Udp()>=0){
                            providerAvailabilityIpv6Udp++;
                        }
                        if(data.getQueryLatencyIpv6Tcp()>=0){
                            providerAvailabilityIpv6Tcp++;
                        }
                    }
                    if(data.getQueryLatencyIpv4Udp()>=0){
                        ipv4Udp.add(data.getQueryLatencyIpv4Udp());
                    }else {
                        ipv4Udp.add(INT_MAX);
                    }
                    if(data.getQueryLatencyIpv4Tcp()>=0){
                        ipv4Tcp.add(data.getQueryLatencyIpv4Tcp());
                    }else {
                        ipv4Tcp.add(INT_MAX);
                    }
                    if(data.getQueryLatencyIpv6Udp()>=0){
                        ipv6Udp.add(data.getQueryLatencyIpv6Udp());
                    }else {
                        ipv6Udp.add(INT_MAX);
                    }
                    if(data.getQueryLatencyIpv6Tcp()>=0){
                        ipv6Tcp.add(data.getQueryLatencyIpv6Tcp());
                    }else {
                        ipv6Tcp.add(INT_MAX);
                    }
                    publicationLatency[publicationLatencyPointer]=data.getPublicationLatency();
                    publicationLatencyPointer++;
                    if(iterator.hasNext()){
                        data=iterator.next();
                    }
                }while (iterator.hasNext()&&data.getProvider().equals(provider));
                availabilityIpv4Udp+=Math.min(numberK,providerAvailabilityIpv4Udp);
                availabilityIpv4Tcp+=Math.min(numberK,providerAvailabilityIpv4Tcp);
                availabilityIpv6Udp+=Math.min(numberK,providerAvailabilityIpv6Udp);
                availabilityIpv6Tcp+=Math.min(numberK,providerAvailabilityIpv6Tcp);
                Collections.sort(ipv4Udp);
                Collections.sort(ipv4Tcp);
                Collections.sort(ipv6Udp);
                Collections.sort(ipv6Tcp);
                for (int i = 0; i < numberK; i++) {
                    ipv4UdpLatency[latencyPointer+i]= ipv4Udp.get(i);
                    ipv4TcpLatency[latencyPointer+i]=ipv4Tcp.get(i);
                    ipv6UdpLatency[latencyPointer+i]= ipv6Udp.get(i);
                    ipv6TcpLatency[latencyPointer+i]=ipv6Tcp.get(i);
                }
                latencyPointer+=numberK;
            }while (iterator.hasNext()&&data.getTimestamp().equals(timeStamp));
        }while (iterator.hasNext());
        Map<String,Object> rMap=new HashMap<>();
        float availabilityIpv4UdpResult=(float)availabilityIpv4Udp/latencyPointer;
        float availabilityIpv4TcpResult=(float)availabilityIpv4Tcp/latencyPointer;
        float availabilityIpv6UdpResult=(float)availabilityIpv6Udp/latencyPointer;
        float availabilityIpv6TcpResult=(float)availabilityIpv6Tcp/latencyPointer;
        rMap.put("availability_ipv4_udp",new PercentageResult(availabilityIpv4UdpResult,dataSize));
        rMap.put("availability_ipv4_tcp",new PercentageResult(availabilityIpv4TcpResult,dataSize));
        rMap.put("availability_ipv6_udp",new PercentageResult(availabilityIpv6UdpResult,dataSize));
        rMap.put("availability_ipv6_tcp",new PercentageResult(availabilityIpv6TcpResult,dataSize));
        rMap.put("response_latency_ipv4_udp",new TimeResult(getMedian(ipv4UdpLatency,latencyPointer),dataSize));
        rMap.put("response_latency_ipv4_tcp",new TimeResult(getMedian(ipv4TcpLatency,latencyPointer),dataSize));
        rMap.put("response_latency_ipv6_udp",new TimeResult(getMedian(ipv6UdpLatency,latencyPointer),dataSize));
        rMap.put("response_latency_ipv6_tcp",new TimeResult(getMedian(ipv6TcpLatency,latencyPointer),dataSize));
        rMap.put("correctness",new PercentageResult((float)correctness/dataSize,dataSize));
        rMap.put("publication_latency",new TimeResult(getMedian(publicationLatency,dataSize),dataSize));
        return  rMap;

    }
    /**
     * rsi可靠性， 列表中的数据应当是关于同一类型服务器的
     * @param dataList
     * @return
     */
    public Map<String,PercentageResult> calRsiAvailability(List<RsiData> dataList){
        int ipv4Udp=0;
        int ipv4Tcp=0;
        int ipv6Udp=0;
        int ipv6Tcp=0;
        int size=dataList.size();
        for(RsiData data :dataList){
            if(data.isCorrectness()){
                if(data.getQueryLatencyIpv4Udp()>=0){
                    ipv4Udp++;
                }
                if(data.getQueryLatencyIpv4Tcp()>=0){
                    ipv4Tcp++;
                }
                if(data.getQueryLatencyIpv6Udp()>=0){
                    ipv6Udp++;
                }
                if(data.getQueryLatencyIpv6Tcp()>=0){
                    ipv6Tcp++;
                }
            }
        }
        Map<String,PercentageResult> rMap=new HashMap<>();
        rMap.put("ipv4_udp",new PercentageResult((float)ipv4Udp/size,size));
        rMap.put("ipv4_tcp",new PercentageResult((float)ipv4Tcp/size,size));
        rMap.put("ipv6_udp",new PercentageResult((float)ipv6Udp/size,size));
        rMap.put("ipv6_tcp",new PercentageResult((float)ipv6Tcp/size,size));
        return  rMap;
    }

    public Map<String, TimeResult> calRsiResponseLatency(List<RsiData> dataList){
        int size=dataList.size();
        int[] ipv4Udp=new int[size];
        int[] ipv4Tcp=new int[size];
        int[] ipv6Udp=new int[size];
        int[] ipv6Tcp=new int[size];
        for (int i = 0; i < size; i++) {
            RsiData data=dataList.get(i);
            ipv4Udp[i]=data.getQueryLatencyIpv4Udp();
            ipv4Tcp[i]=data.getQueryLatencyIpv4Tcp();
            ipv6Udp[i]=data.getQueryLatencyIpv6Udp();
            ipv6Tcp[i]=data.getQueryLatencyIpv6Tcp();
        }
        Map<String,TimeResult> rMap=new HashMap<>();
        rMap.put("ipv4_udp",new TimeResult(getMedian(ipv4Udp,size),size));
        rMap.put("ipv4_tcp",new TimeResult(getMedian(ipv4Tcp,size),size));
        rMap.put("ipv6_udp",new TimeResult(getMedian(ipv6Udp,size),size));
        rMap.put("ipv6_tcp",new TimeResult(getMedian(ipv6Tcp,size),size));
        return  rMap;

    }

    public PercentageResult calRsiCorrectness(List<RsiData> dataList){
        int sum=0;
        for(RsiData data:dataList){
            if(data.isCorrectness()){
                sum++;
            }
        }
        return new PercentageResult((float)sum/dataList.size(),dataList.size());
    }

    public TimeResult calRsiPublicationLatency(List<RsiData> dataList){
        int size= dataList.size();
        int[] list=new int[size];
        for (int i = 0; i < size; i++) {
            list[i]=dataList.get(i).getPublicationLatency();
        }
        return new TimeResult(getMedian(list,size),size);
    }


    public float getMedian(int[] list,int size){
        if(size%2!=0){
            return (float) SelectKth.BFPRT(list,0,size-1,1+size/2);
        }else {
            return (float)(SelectKth.BFPRT(list,0,size-1,size/2)
                    +SelectKth.BFPRT(list,0,size-1,1+size/2))/2;
        }
    }


}
