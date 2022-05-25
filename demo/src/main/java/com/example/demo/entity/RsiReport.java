package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;


public class RsiReport {

    @TableId(value = "timestamp_ymd")
    private String timestamp;//时间戳
    @TableId(value = "name_")
    private String name_;//根类型

    @TableField(value = "ipv4_udp_availability")
    private double availabilityIpv4Udp;
    @TableField(value = "ipv4_tcp_availability")
    private double availabilityIpv4Tcp;
    @TableField(value = "ipv6_udp_availability")
    private double availabilityIpv6Udp;
    @TableField(value = "ipv6_tcp_availability")
    private double availabilityIpv6Tcp;
    //延迟单位为毫秒
    @TableField(value = "ipv4_udp_latency")
    private int latencyIpv4Udp;
    @TableField(value = "ipv4_tcp_latency")
    private int latencyIpv4Tcp;
    @TableField(value = "ipv6_udp_latency")
    private int latencyIpv6Udp;
    @TableField(value = "ipv6_tcp_latency")
    private int latencyIpv6Tcp;
    @TableField(value = "correctness")
    private double correctness;
    @TableField(value = "publication_latency")
    private int publicationLatency;//单位为毫秒
    @TableField(value = "ipv4_udp_availability_measurements")
    private int Ipv4UdpAvailabilityMeasurements;
    @TableField(value = "ipv4_tcp_availability_measurements")
    private int Ipv4TcpAvailabilityMeasurements;
    @TableField(value = "ipv6_udp_availability_measurements")
    private int Ipv6UdpAvailabilityMeasurements;
    @TableField(value = "ipv6_tcp_availability_measurements")
    private int Ipv6TcpAvailabilityMeasurements;
    @TableField(value = "ipv4_udp_latency_measurements")
    private int Ipv4UdpLatencyMeasurements;
    @TableField(value = "ipv4_tcp_latency_measurements")
    private int Ipv4TcpLatencyMeasurements;
    @TableField(value = "ipv6_udp_latency_measurements")
    private int Ipv6UdpLatencyMeasurements;
    @TableField(value = "ipv6_tcp_latency_measurements")
    private int Ipv6TcpLatencyMeasurements;
    @TableField(value = "correctness_measurements")
    private int correctnessMeasurements;
    @TableField(value = "publication_latency_measurements")
    private int publicationLatencyMeasurements;

    public RsiReport() {
    }

    public RsiReport(String timestamp, String name, double availabilityIpv4Udp, double availabilityIpv4Tcp,
                     double availabilityIpv6Udp, double availabilityIpv6Tcp, int latencyIpv4Udp, int latencyIpv4Tcp,
                     int latencyIpv6Udp, int latencyIpv6Tcp, double correctness, int publicationLatency,
                     int ipv4UdpAvailabilityMeasurements, int ipv4TcpAvailabilityMeasurements,
                     int ipv6UdpAvailabilityMeasurements, int ipv6TcpAvailabilityMeasurements,
                     int ipv4UdpLatencyMeasurements, int ipv4TcpLatencyMeasurements, int ipv6UdpLatencyMeasurements,
                     int ipv6TcpLatencyMeasurements, int correctnessMeasurements, int publicationLatencyMeasurements) {
        this.timestamp = timestamp;
        this.name_ = name;
        this.availabilityIpv4Udp = availabilityIpv4Udp;
        this.availabilityIpv4Tcp = availabilityIpv4Tcp;
        this.availabilityIpv6Udp = availabilityIpv6Udp;
        this.availabilityIpv6Tcp = availabilityIpv6Tcp;
        this.latencyIpv4Udp = latencyIpv4Udp;
        this.latencyIpv4Tcp = latencyIpv4Tcp;
        this.latencyIpv6Udp = latencyIpv6Udp;
        this.latencyIpv6Tcp = latencyIpv6Tcp;
        this.correctness = correctness;
        this.publicationLatency = publicationLatency;
        Ipv4UdpAvailabilityMeasurements = ipv4UdpAvailabilityMeasurements;
        Ipv4TcpAvailabilityMeasurements = ipv4TcpAvailabilityMeasurements;
        Ipv6UdpAvailabilityMeasurements = ipv6UdpAvailabilityMeasurements;
        Ipv6TcpAvailabilityMeasurements = ipv6TcpAvailabilityMeasurements;
        Ipv4UdpLatencyMeasurements = ipv4UdpLatencyMeasurements;
        Ipv4TcpLatencyMeasurements = ipv4TcpLatencyMeasurements;
        Ipv6UdpLatencyMeasurements = ipv6UdpLatencyMeasurements;
        Ipv6TcpLatencyMeasurements = ipv6TcpLatencyMeasurements;
        this.correctnessMeasurements = correctnessMeasurements;
        this.publicationLatencyMeasurements = publicationLatencyMeasurements;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name_;
    }

    public void setName(String name) {
        this.name_ = name;
    }

    public double getAvailabilityIpv4Udp() {
        return availabilityIpv4Udp;
    }

    public void setAvailabilityIpv4Udp(double availabilityIpv4Udp) {
        this.availabilityIpv4Udp = availabilityIpv4Udp;
    }

    public double getAvailabilityIpv4Tcp() {
        return availabilityIpv4Tcp;
    }

    public void setAvailabilityIpv4Tcp(double availabilityIpv4Tcp) {
        this.availabilityIpv4Tcp = availabilityIpv4Tcp;
    }

    public double getAvailabilityIpv6Udp() {
        return availabilityIpv6Udp;
    }

    public void setAvailabilityIpv6Udp(double availabilityIpv6Udp) {
        this.availabilityIpv6Udp = availabilityIpv6Udp;
    }

    public double getAvailabilityIpv6Tcp() {
        return availabilityIpv6Tcp;
    }

    public void setAvailabilityIpv6Tcp(double availabilityIpv6Tcp) {
        this.availabilityIpv6Tcp = availabilityIpv6Tcp;
    }

    public int getLatencyIpv4Udp() {
        return latencyIpv4Udp;
    }

    public void setLatencyIpv4Udp(int latencyIpv4Udp) {
        this.latencyIpv4Udp = latencyIpv4Udp;
    }

    public int getLatencyIpv4Tcp() {
        return latencyIpv4Tcp;
    }

    public void setLatencyIpv4Tcp(int latencyIpv4Tcp) {
        this.latencyIpv4Tcp = latencyIpv4Tcp;
    }

    public int getLatencyIpv6Udp() {
        return latencyIpv6Udp;
    }

    public void setLatencyIpv6Udp(int latencyIpv6Udp) {
        this.latencyIpv6Udp = latencyIpv6Udp;
    }

    public int getLatencyIpv6Tcp() {
        return latencyIpv6Tcp;
    }

    public void setLatencyIpv6Tcp(int latencyIpv6Tcp) {
        this.latencyIpv6Tcp = latencyIpv6Tcp;
    }

    public double getCorrectness() {
        return correctness;
    }

    public void setCorrectness(double correctness) {
        this.correctness = correctness;
    }

    public int getPublicationLatency() {
        return publicationLatency;
    }

    public void setPublicationLatency(int publicationLatency) {
        this.publicationLatency = publicationLatency;
    }

    public int getIpv4UdpAvailabilityMeasurements() {
        return Ipv4UdpAvailabilityMeasurements;
    }

    public void setIpv4UdpAvailabilityMeasurements(int ipv4UdpAvailabilityMeasurements) {
        Ipv4UdpAvailabilityMeasurements = ipv4UdpAvailabilityMeasurements;
    }

    public int getIpv4TcpAvailabilityMeasurements() {
        return Ipv4TcpAvailabilityMeasurements;
    }

    public void setIpv4TcpAvailabilityMeasurements(int ipv4TcpAvailabilityMeasurements) {
        Ipv4TcpAvailabilityMeasurements = ipv4TcpAvailabilityMeasurements;
    }

    public int getIpv6UdpAvailabilityMeasurements() {
        return Ipv6UdpAvailabilityMeasurements;
    }

    public void setIpv6UdpAvailabilityMeasurements(int ipv6UdpAvailabilityMeasurements) {
        Ipv6UdpAvailabilityMeasurements = ipv6UdpAvailabilityMeasurements;
    }

    public int getIpv6TcpAvailabilityMeasurements() {
        return Ipv6TcpAvailabilityMeasurements;
    }

    public void setIpv6TcpAvailabilityMeasurements(int ipv6TcpAvailabilityMeasurements) {
        Ipv6TcpAvailabilityMeasurements = ipv6TcpAvailabilityMeasurements;
    }

    public int getIpv4UdpLatencyMeasurements() {
        return Ipv4UdpLatencyMeasurements;
    }

    public void setIpv4UdpLatencyMeasurements(int ipv4UdpLatencyMeasurements) {
        Ipv4UdpLatencyMeasurements = ipv4UdpLatencyMeasurements;
    }

    public int getIpv4TcpLatencyMeasurements() {
        return Ipv4TcpLatencyMeasurements;
    }

    public void setIpv4TcpLatencyMeasurements(int ipv4TcpLatencyMeasurements) {
        Ipv4TcpLatencyMeasurements = ipv4TcpLatencyMeasurements;
    }

    public int getIpv6UdpLatencyMeasurements() {
        return Ipv6UdpLatencyMeasurements;
    }

    public void setIpv6UdpLatencyMeasurements(int ipv6UdpLatencyMeasurements) {
        Ipv6UdpLatencyMeasurements = ipv6UdpLatencyMeasurements;
    }

    public int getIpv6TcpLatencyMeasurements() {
        return Ipv6TcpLatencyMeasurements;
    }

    public void setIpv6TcpLatencyMeasurements(int ipv6TcpLatencyMeasurements) {
        Ipv6TcpLatencyMeasurements = ipv6TcpLatencyMeasurements;
    }

    public int getCorrectnessMeasurements() {
        return correctnessMeasurements;
    }

    public void setCorrectnessMeasurements(int correctnessMeasurements) {
        this.correctnessMeasurements = correctnessMeasurements;
    }

    public int getPublicationLatencyMeasurements() {
        return publicationLatencyMeasurements;
    }

    public void setPublicationLatencyMeasurements(int publicationLatencyMeasurements) {
        this.publicationLatencyMeasurements = publicationLatencyMeasurements;
    }
}
