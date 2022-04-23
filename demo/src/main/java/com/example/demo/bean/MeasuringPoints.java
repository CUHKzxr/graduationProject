package com.example.demo.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

public class MeasuringPoints {
    @TableId(value = "identification")
    private String identification;
    @TableField(value = "location")
    private String location;
    @TableField(value = "ipv4_address")
    private int ipv4Address;
    @TableField(value = "ipv6_address")
    private byte[] ipv6Address;

    /*
        normal:正常
        1lost:丢失1次数据
        2lost:丢失两次数据
        offline:离线（丢失三次数据）
        abandoned:已丢弃的节点
     */
    @TableField(value = "status")
    private String status;

    public MeasuringPoints() {
    }

    public MeasuringPoints(String identification, String location, int ipv4Address, byte[] ipv6Address, String status) {
        this.identification = identification;
        this.location = location;
        this.ipv4Address = ipv4Address;
        this.ipv6Address = ipv6Address;
        this.status = status;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getIpv4Address() {
        return ipv4Address;
    }

    public void setIpv4Address(int ipv4Address) {
        this.ipv4Address = ipv4Address;
    }

    public byte[] getIpv6Address() {
        return ipv6Address;
    }

    public void setIpv6Address(byte[] ipv6Address) {
        this.ipv6Address = ipv6Address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
