package com.example.demo.report;

public class BooleanResult {
    private final boolean value;
    private final int measurements;

    public BooleanResult(boolean value, int measurements) {
        this.value = value;
        this.measurements = measurements;
    }

    public boolean isValue() {
        return value;
    }

    public int getMeasurements() {
        return measurements;
    }
}
