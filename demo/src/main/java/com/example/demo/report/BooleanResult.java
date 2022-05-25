package com.example.demo.report;

public class BooleanResult {
    private final int value;
    private final int measurements;

    public BooleanResult(boolean value, int measurements) {
        this.value = value?1:0;
        this.measurements = measurements;
    }

    public int getValue() {
        return value;
    }

    public int getMeasurements() {
        return measurements;
    }
}
