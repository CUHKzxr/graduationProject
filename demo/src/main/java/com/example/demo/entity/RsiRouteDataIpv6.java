package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("rsi_route_data_ipv6")
public class RsiRouteDataIpv6 {
    @TableId(value = "timestamp_")
    private String timestamp;
    @TableId(value = "provider")
    private String provider;
    @TableId(value = "name_")
    private char name_;
    @TableId(value = "identification")
    private String identification;
    @TableId(value = "index_")
    private int index_;
    @TableField(value = "address")
    private byte[] address;

    public RsiRouteDataIpv6(String timestamp, String provider, char name, String identification,int index, byte[] address) {
        this.timestamp = timestamp;
        this.provider = provider;
        this.name_ = name;
        this.identification=identification;
        this.index_ = index;
        this.address = address;
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

    public char getName_() {
        return name_;
    }

    public void setName_(char name_) {
        this.name_ = name_;
    }

    public int getIndex() {
        return index_;
    }

    public void setIndex(int index) {
        this.index_ = index;
    }

    public byte[] getAddress() {
        return address;
    }

    public void setAddress(byte[] address) {
        this.address = address;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }
}
