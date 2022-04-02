package com.example.demo.utils.report;

public class PercentageResult {
    private static final double rsiAvailabilityThreshold = 0.96;
    private static final double rsiCorrectnessThreshold = 1;
    private static final double rssAvailabilityThreshold = 0.99999;
    private static final double rssCorrectnessThreshold = 1;
    private final double value;
    private final int measurementsSize;


    public PercentageResult(double value, int measurementsSize) {
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
