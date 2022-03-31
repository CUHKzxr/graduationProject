package com.example.demo.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("rsi_route_data_ipv4")
public class RsiRouteDataIpv4 {
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
    private int address;

    public RsiRouteDataIpv4(String timestamp, String provider, char name,String identification, int index, int address) {
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

    public char getName() {
        return name_;
    }

    public void setName(char name) {
        this.name_ = name;
    }

    public int getIndex() {
        return index_;
    }

    public void setIndex(int index) {
        this.index_ = index;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }
}


