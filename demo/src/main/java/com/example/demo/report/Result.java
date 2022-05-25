package com.example.demo.report;

public class Result {
    private final double value;
    private final int measurementsSize;


    public Result(double value, int measurementsSize) {
        this.value = value;
        this.measurementsSize = measurementsSize;
    }

    public double getValue() {
        return value;
    }

    public int getMeasurementsSize() {
        return measurementsSize;
    }
}
