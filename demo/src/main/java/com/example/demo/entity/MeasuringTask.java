package com.example.demo.entity;

public class MeasuringTask {
    private int id;
    private String identification;
    private boolean effective;
    private String startTimestamp;
    private String endTimestamp;
    private String text;

    public MeasuringTask() {
    }

    public MeasuringTask(int id, String identification, boolean effective, String startTimestamp, String endTimestamp, String text) {
        this.id = id;
        this.identification = identification;
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
}
