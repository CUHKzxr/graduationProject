package com.example.demo.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

public class RsiDataWithIpv6 {
    private String timestamp;
    private String provider;
    private char name_;
    private String identification;
    private int queryLatencyIpv4Udp;
    private int queryLatencyIpv4Tcp;
    private int queryLatencyIpv6Udp;
    private int queryLatencyIpv6Tcp;
    private int pathCountIpv4;
    private int pathCountIpv6;
    private String status;
    private int sourceIpIpv4;
    private byte[] sourceIpIpv6;
    private int referLatencyIpv4_0;
    private int referLatencyIpv4_1;
    private int referLatencyIpv4_2;
    private int referLatencyIpv6_0;
    private int referLatencyIpv6_1;
    private int referLatencyIpv6_2;
    private int publicationLatency;
    private boolean correctness;
    private int index;
    private byte[] ipv6RouteAddress;

    public RsiDataWithIpv6(){

    }

    public RsiDataWithIpv6(String timestamp, String provider, char name, String identification,
                           int[] queryLatency, int[] pathCount, String status,
                           int sourceIpv4, byte[] sourceIpv6, int[] referLatency,
                           boolean correctness, int publicationLatency) {
        this.timestamp = timestamp;
        this.provider = provider;
        this.name_ = name;
        this.identification = identification;
        this.queryLatencyIpv4Udp = queryLatency[0];
        this.queryLatencyIpv4Tcp = queryLatency[1];
        this.queryLatencyIpv6Udp = queryLatency[2];
        this.queryLatencyIpv6Tcp = queryLatency[3];
        this.pathCountIpv4 = pathCount[0];
        this.pathCountIpv6 = pathCount[1];
        this.status = status;
        this.sourceIpIpv4 = sourceIpv4;
        this.sourceIpIpv6 = sourceIpv6;
        this.referLatencyIpv4_0 = referLatency[0];
        this.referLatencyIpv4_1 = referLatency[1];
        this.referLatencyIpv4_2 = referLatency[2];
        this.referLatencyIpv6_0 = referLatency[0];
        this.referLatencyIpv6_1 = referLatency[1];
        this.referLatencyIpv6_2 = referLatency[2];
        this.correctness=correctness;
        this.publicationLatency=publicationLatency;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public char getName() {
        return name_;
    }

    public void setName(char name) {
        this.name_ = name;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public int getQueryLatencyIpv4Udp() {
        return queryLatencyIpv4Udp;
    }

    public void setQueryLatencyIpv4Udp(int queryLatencyIpv4Udp) {
        this.queryLatencyIpv4Udp = queryLatencyIpv4Udp;
    }

    public int getQueryLatencyIpv4Tcp() {
        return queryLatencyIpv4Tcp;
    }

    public void setQueryLatencyIpv4Tcp(int queryLatencyIpv4Tcp) {
        this.queryLatencyIpv4Tcp = queryLatencyIpv4Tcp;
    }

    public int getQueryLatencyIpv6Udp() {
        return queryLatencyIpv6Udp;
    }

    public void setQueryLatencyIpv6Udp(int queryLatencyIpv6Udp) {
        this.queryLatencyIpv6Udp = queryLatencyIpv6Udp;
    }

    public int getQueryLatencyIpv6Tcp() {
        return queryLatencyIpv6Tcp;
    }

    public void setQueryLatencyIpv6Tcp(int queryLatencyIpv6Tcp) {
        this.queryLatencyIpv6Tcp = queryLatencyIpv6Tcp;
    }

    public int getPathCountIpv4() {
        return pathCountIpv4;
    }

    public void setPathCountIpv4(int pathCountIpv4) {
        this.pathCountIpv4 = pathCountIpv4;
    }

    public int getPathCountIpv6() {
        return pathCountIpv6;
    }

    public void setPathCountIpv6(int pathCountIpv6) {
        this.pathCountIpv6 = pathCountIpv6;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSourceIpIpv4() {
        return sourceIpIpv4;
    }

    public void setSourceIpIpv4(int sourceIpIpv4) {
        this.sourceIpIpv4 = sourceIpIpv4;
    }

    public byte[] getSourceIpIpv6() {
        return sourceIpIpv6;
    }

    public void setSourceIpIpv6(byte[] sourceIpIpv6) {
        this.sourceIpIpv6 = sourceIpIpv6;
    }

    public int getReferLatencyIpv4_0() {
        return referLatencyIpv4_0;
    }

    public void setReferLatencyIpv4_0(int referLatencyIpv4_0) {
        this.referLatencyIpv4_0 = referLatencyIpv4_0;
    }

    public int getReferLatencyIpv4_1() {
        return referLatencyIpv4_1;
    }

    public void setReferLatencyIpv4_1(int referLatencyIpv4_1) {
        this.referLatencyIpv4_1 = referLatencyIpv4_1;
    }

    public int getReferLatencyIpv4_2() {
        return referLatencyIpv4_2;
    }

    public void setReferLatencyIpv4_2(int referLatencyIpv4_2) {
        this.referLatencyIpv4_2 = referLatencyIpv4_2;
    }

    public int getReferLatencyIpv6_0() {
        return referLatencyIpv6_0;
    }

    public void setReferLatencyIpv6_0(int referLatencyIpv6_0) {
        this.referLatencyIpv6_0 = referLatencyIpv6_0;
    }

    public int getReferLatencyIpv6_1() {
        return referLatencyIpv6_1;
    }

    public void setReferLatencyIpv6_1(int referLatencyIpv6_1) {
        this.referLatencyIpv6_1 = referLatencyIpv6_1;
    }

    public int getReferLatencyIpv6_2() {
        return referLatencyIpv6_2;
    }

    public void setReferLatencyIpv6_2(int referLatencyIpv6_2) {
        this.referLatencyIpv6_2 = referLatencyIpv6_2;
    }

    public int getPublicationLatency() {
        return publicationLatency;
    }

    public void setPublicationLatency(int publicationLatency) {
        this.publicationLatency = publicationLatency;
    }

    public boolean isCorrectness() {
        return correctness;
    }

    public void setCorrectness(boolean correctness) {
        this.correctness = correctness;
    }

    public byte[] getIpv6RouteAddress() {
        return ipv6RouteAddress;
    }

    public void setIpv6RouteAddress(byte[] ipv6RouteAddress) {
        this.ipv6RouteAddress = ipv6RouteAddress;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
