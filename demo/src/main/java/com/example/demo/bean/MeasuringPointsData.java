package com.example.demo.bean;

public class MeasuringPointsData {

    private String identification;
    private int id;
    private String location;
    private int ipv4Address;
    private byte[] ipv6Address;
    private String status;
    private boolean effective;
    private String startTimestamp;
    private String endTimestamp;
    private String text;

    public MeasuringPointsData() {
    }

    public MeasuringPointsData(String identification, int id, String location, int ipv4Address, byte[] ipv6Address,
                               String status, boolean effective, String startTimestamp, String endTimestamp,
                               String text) {
        this.identification = identification;
        this.id = id;
        this.location = location;
        this.ipv4Address = ipv4Address;
        this.ipv6Address = ipv6Address;
        this.status = status;
        this.effective = effective;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public boolean isEffective() {
        return effective;
    }

    public void setEffective(boolean effective) {
        this.effective = effective;
    }

    public String getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(String startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public String getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(String endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }

        if(obj == null || getClass() != obj.getClass()){
            return false;
        }
        MeasuringPointsData measuringPointsData=(MeasuringPointsData) obj;
        return this.identification.equals(measuringPointsData.getIdentification())
                &&(this.id == measuringPointsData.getId());

    }
}
