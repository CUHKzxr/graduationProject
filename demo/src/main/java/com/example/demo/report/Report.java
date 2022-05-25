package com.example.demo.report;

import com.example.demo.entity.RsiData;

import java.util.*;

public class Report {
    private static final int numberK = 8;
    private static final int INT_MAX = FindMedian.INT_MAX;
    private static final FindMedian findMedian=new QuickSort();

    //public Map<String,BooleanResult>

    /**
     * @param dataList 应当仅包含同一根类型的数据
     * @return
     */
    public static Map<String, Object> calRsiReport(List<RsiData> dataList, char name) {
        if(dataList.size()==0){
            throw new IllegalArgumentException("dataList should not be empty,because you can't generate a report from an empty datasource");
        }
        int availabilityIpv4Udp = 0;
        int availabilityIpv4Tcp = 0;
        int availabilityIpv6Udp = 0;
        int availabilityIpv6Tcp = 0;
        int correctness = 0;
        int size = dataList.size();
        int[] ipv4UdpLatency = new int[size];
        int[] ipv4TcpLatency = new int[size];
        int[] ipv6UdpLatency = new int[size];
        int[] ipv6TcpLatency = new int[size];
        int[] publicationLatency = new int[size];
        int ipv4Size=0,ipv6Size=0;
        for (int i = 0; i < size; i++) {
            RsiData data = dataList.get(i);
            //ipv4 part


            ipv4UdpLatency[i] = data.getQueryLatencyIpv4Udp()>=0?data.getQueryLatencyIpv4Udp():INT_MAX;
            ipv4TcpLatency[i] = data.getQueryLatencyIpv4Tcp()>=0?data.getQueryLatencyIpv4Tcp():INT_MAX;
            ipv6UdpLatency[i] = data.getQueryLatencyIpv6Udp()>=0?data.getQueryLatencyIpv6Udp():INT_MAX;
            ipv6TcpLatency[i] = data.getQueryLatencyIpv6Tcp()>-0?data.getQueryLatencyIpv6Tcp():INT_MAX;
            publicationLatency[i] = data.getPublicationLatency()>=0?data.getPublicationLatency():INT_MAX;
            if (data.isCorrectness()) {
                correctness++;
                if (data.getQueryLatencyIpv4Udp() >= 0) {
                    availabilityIpv4Udp++;
                }
                if (data.getQueryLatencyIpv4Tcp() >= 0) {
                    availabilityIpv4Tcp++;
                }
                if (data.getQueryLatencyIpv6Udp() >= 0) {
                    availabilityIpv6Udp++;
                }
                if (data.getQueryLatencyIpv6Tcp() >= 0) {
                    availabilityIpv6Tcp++;
                }
            }
        }
        Map<String, Object> rMap = new HashMap<>();
        rMap.put("name", name);
        rMap.put("ipv4_udp_availability", new Result((double) availabilityIpv4Udp / size, size));
        rMap.put("ipv4_tcp_availability", new Result((double) availabilityIpv4Tcp / size, size));
        rMap.put("ipv6_udp_availability", new Result((double) availabilityIpv6Udp / size, size));
        rMap.put("ipv6_tcp_availability", new Result((double) availabilityIpv6Tcp / size, size));
        rMap.put("ipv4_udp_latency", new Result(getMedian(ipv4UdpLatency, size), size));
        rMap.put("ipv4_tcp_latency", new Result(getMedian(ipv4TcpLatency, size), size));
        rMap.put("ipv6_udp_latency", new Result(getMedian(ipv6UdpLatency, size), size));
        rMap.put("ipv6_tcp_latency", new Result(getMedian(ipv6TcpLatency, size), size));
        rMap.put("correctness", new Result((double) correctness / size, size));
        rMap.put("publication_latency", new Result(getMedian(publicationLatency, size), size));
        return rMap;
    }



    public static Map<String, Object> calRsiReportBoolean(List<RsiData> dataList, char name) {
        int availabilityIpv4Udp = 0;
        int availabilityIpv4Tcp = 0;
        int availabilityIpv6Udp = 0;
        int availabilityIpv6Tcp = 0;
        int correctness = 0;
        int size = dataList.size();
        int[] ipv4UdpLatency = new int[size];
        int[] ipv4TcpLatency = new int[size];
        int[] ipv6UdpLatency = new int[size];
        int[] ipv6TcpLatency = new int[size];
        int[] publicationLatency = new int[size];
        for (int i = 0; i < size; i++) {
            RsiData data = dataList.get(i);
            ipv4UdpLatency[i] = data.getQueryLatencyIpv4Udp()>=0?data.getQueryLatencyIpv4Udp():INT_MAX;
            ipv4TcpLatency[i] = data.getQueryLatencyIpv4Tcp()>=0?data.getQueryLatencyIpv4Tcp():INT_MAX;
            ipv6UdpLatency[i] = data.getQueryLatencyIpv6Udp()>=0?data.getQueryLatencyIpv6Udp():INT_MAX;
            ipv6TcpLatency[i] = data.getQueryLatencyIpv6Tcp()>-0?data.getQueryLatencyIpv6Tcp():INT_MAX;
            publicationLatency[i] = data.getPublicationLatency()>=0?data.getPublicationLatency():INT_MAX;
            if (data.isCorrectness()) {
                correctness++;
                if (data.getQueryLatencyIpv4Udp() >= 0) {
                    availabilityIpv4Udp++;
                }
                if (data.getQueryLatencyIpv4Tcp() >= 0) {
                    availabilityIpv4Tcp++;
                }
                if (data.getQueryLatencyIpv6Udp() >= 0) {
                    availabilityIpv6Udp++;
                }
                if (data.getQueryLatencyIpv6Tcp() >= 0) {
                    availabilityIpv6Tcp++;
                }
            }
        }
        Map<String, Object> rMap = new HashMap<>();
        rMap.put("name", name);
        rMap.put("ipv4_udp_availability", new BooleanResult(((double) availabilityIpv4Udp / size)>=0.96, size));
        rMap.put("ipv4_tcp_availability", new BooleanResult(((double) availabilityIpv4Tcp / size)>=0.96, size));
        rMap.put("ipv6_udp_availability", new BooleanResult(((double) availabilityIpv6Udp / size)>=0.96, size));
        rMap.put("ipv6_tcp_availability", new BooleanResult(((double) availabilityIpv6Tcp / size)>=0.96, size));
        rMap.put("ipv4_udp_latency", new BooleanResult(getMedian(ipv4UdpLatency, size)<=250, size));
        rMap.put("ipv4_tcp_latency", new BooleanResult(getMedian(ipv4TcpLatency, size)<=500, size));
        rMap.put("ipv6_udp_latency", new BooleanResult(getMedian(ipv6UdpLatency, size)<=250, size));
        rMap.put("ipv6_tcp_latency", new BooleanResult(getMedian(ipv6TcpLatency, size)<=500, size));
        rMap.put("correctness", new BooleanResult(checkCorrectness((double) correctness/size), size));
        rMap.put("publication_latency", new BooleanResult(getMedian(publicationLatency, size)<=3900000, size));
        return rMap;
    }

    /**
     * 传入的数据应当已经按照时间、节点、类型的顺序排序
     *
     * @param dataList
     * @return
     */
    public static Map<String, Object> calRssReport(List<RsiData> dataList) {
        if(dataList.size()==0){
            throw new IllegalArgumentException("dataList should not be empty,because you can't generate a report from an empty datasource");
        }
        int availabilityIpv4Udp = 0;
        int availabilityIpv4Tcp = 0;
        int availabilityIpv6Udp = 0;
        int availabilityIpv6Tcp = 0;
        int correctness = 0;
        int dataSize = dataList.size();
        int[] ipv4UdpLatency = new int[dataSize];
        int[] ipv4TcpLatency = new int[dataSize];
        int[] ipv6UdpLatency = new int[dataSize];
        int[] ipv6TcpLatency = new int[dataSize];
        int latencyPointer = 0;
        int publicationLatencyPointer = 0;
        int[] publicationLatency = new int[dataSize];
        Iterator<RsiData> iterator = dataList.listIterator();
        String timeStamp = null;
        String provider = null;
        int providerAvailabilityIpv4Udp =0;
        int providerAvailabilityIpv4Tcp =0;
        int providerAvailabilityIpv6Udp =0;
        int providerAvailabilityIpv6Tcp =0;
        List<Integer> providerIpv4Udp = new ArrayList<>(13);
        List<Integer> providerIpv4Tcp = new ArrayList<>(13);
        List<Integer> providerIpv6Udp = new ArrayList<>(13);
        List<Integer> providerIpv6Tcp = new ArrayList<>(13);
        RsiData data=dataList.get(0);
        String preTimeStamp=data.getTimestamp();
        String preProvider= data.getProvider();
        while (iterator.hasNext()){
            data=iterator.next();
            String tempTimestamp=data.getTimestamp();
            String tempProvider=data.getProvider();
            if((!tempTimestamp.equals(preTimeStamp)) ||( !tempProvider.equals(preProvider))){
                availabilityIpv4Udp += Math.min(numberK, providerAvailabilityIpv4Udp);
                availabilityIpv4Tcp += Math.min(numberK, providerAvailabilityIpv4Tcp);
                availabilityIpv6Udp += Math.min(numberK, providerAvailabilityIpv6Udp);
                availabilityIpv6Tcp += Math.min(numberK, providerAvailabilityIpv6Tcp);
                Collections.sort(providerIpv4Udp);
                Collections.sort(providerIpv4Tcp);
                Collections.sort(providerIpv6Udp);
                Collections.sort(providerIpv6Tcp);
                for (int i = 0; i < numberK; i++) {
                    ipv4UdpLatency[latencyPointer + i] = providerIpv4Udp.get(i);
                    ipv4TcpLatency[latencyPointer + i] = providerIpv4Tcp.get(i);
                    ipv6UdpLatency[latencyPointer + i] = providerIpv6Udp.get(i);
                    ipv6TcpLatency[latencyPointer + i] = providerIpv6Tcp.get(i);
                }
                latencyPointer += numberK;

                preTimeStamp=tempTimestamp;
                preProvider=tempProvider;
                providerAvailabilityIpv4Udp=0;
                providerAvailabilityIpv4Tcp=0;
                providerAvailabilityIpv6Udp=0;
                providerAvailabilityIpv6Tcp=0;
                providerIpv4Udp.clear();
                providerIpv4Tcp.clear();
                providerIpv6Udp.clear();
                providerIpv6Tcp.clear();
            }
            if (data.isCorrectness()) {
                correctness++;
                if (data.getQueryLatencyIpv4Udp() >= 0) {
                    providerAvailabilityIpv4Udp++;
                }
                if (data.getQueryLatencyIpv4Tcp() >= 0) {
                    providerAvailabilityIpv4Tcp++;
                }
                if (data.getQueryLatencyIpv6Udp() >= 0) {
                    providerAvailabilityIpv6Udp++;
                }
                if (data.getQueryLatencyIpv6Tcp() >= 0) {
                    providerAvailabilityIpv6Tcp++;
                }
            }
            providerIpv4Udp.add(data.getQueryLatencyIpv4Udp() >= 0?data.getQueryLatencyIpv4Udp():INT_MAX);
            providerIpv4Tcp.add(data.getQueryLatencyIpv4Tcp() >= 0?data.getQueryLatencyIpv4Tcp():INT_MAX);
            providerIpv6Udp.add(data.getQueryLatencyIpv6Udp() >= 0?data.getQueryLatencyIpv6Udp():INT_MAX);
            providerIpv6Tcp.add(data.getQueryLatencyIpv6Tcp() >= 0?data.getQueryLatencyIpv6Tcp():INT_MAX);
            publicationLatency[publicationLatencyPointer] = data.getPublicationLatency()>=0? data.getPublicationLatency():INT_MAX;
            publicationLatencyPointer++;
        }
        availabilityIpv4Udp += Math.min(numberK, providerAvailabilityIpv4Udp);
        availabilityIpv4Tcp += Math.min(numberK, providerAvailabilityIpv4Tcp);
        availabilityIpv6Udp += Math.min(numberK, providerAvailabilityIpv6Udp);
        availabilityIpv6Tcp += Math.min(numberK, providerAvailabilityIpv6Tcp);
        Collections.sort(providerIpv4Udp);
        Collections.sort(providerIpv4Tcp);
        Collections.sort(providerIpv6Udp);
        Collections.sort(providerIpv6Tcp);
        for (int i = 0; i < numberK; i++) {
            ipv4UdpLatency[latencyPointer + i] = providerIpv4Udp.get(i);
            ipv4TcpLatency[latencyPointer + i] = providerIpv4Tcp.get(i);
            ipv6UdpLatency[latencyPointer + i] = providerIpv6Udp.get(i);
            ipv6TcpLatency[latencyPointer + i] = providerIpv6Tcp.get(i);
        }
        latencyPointer += numberK;

        Map<String, Object> rMap = new HashMap<>();
        double availabilityIpv4UdpResult = (double) availabilityIpv4Udp / latencyPointer;
        double availabilityIpv4TcpResult = (double) availabilityIpv4Tcp / latencyPointer;
        double availabilityIpv6UdpResult = (double) availabilityIpv6Udp / latencyPointer;
        double availabilityIpv6TcpResult = (double) availabilityIpv6Tcp / latencyPointer;
        rMap.put("ipv4_udp_availability", new Result(availabilityIpv4UdpResult, dataSize));
        rMap.put("ipv4_tcp_availability", new Result(availabilityIpv4TcpResult, dataSize));
        rMap.put("ipv6_udp_availability", new Result(availabilityIpv6UdpResult, dataSize));
        rMap.put("ipv6_tcp_availability", new Result(availabilityIpv6TcpResult, dataSize));
        rMap.put("ipv4_udp_latency", new Result(getMedian(ipv4UdpLatency, latencyPointer), dataSize));
        rMap.put("ipv4_tcp_latency", new Result(getMedian(ipv4TcpLatency, latencyPointer), dataSize));
        rMap.put("ipv6_udp_latency", new Result(getMedian(ipv6UdpLatency, latencyPointer), dataSize));
        rMap.put("ipv6_tcp_latency", new Result(getMedian(ipv6TcpLatency, latencyPointer), dataSize));
        rMap.put("correctness", new Result((double) correctness / dataSize, dataSize));
        rMap.put("publication_latency", new Result(getMedian(publicationLatency, dataSize), dataSize));
        return rMap;

    }

    private static int getMedian(int[] list, int size) {
        return findMedian.getMedian(list,0,size-1);
    }



    public static boolean checkCorrectness(double correctness){
        return -1e-6 <= correctness - 1 && correctness - 1 <= 1e-6;
    }

}
