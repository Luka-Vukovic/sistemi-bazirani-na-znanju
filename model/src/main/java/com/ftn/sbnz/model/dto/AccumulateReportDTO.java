package com.ftn.sbnz.model.dto;

public class AccumulateReportDTO {

    private long totalDelayMinutes;
    private long delayedFlightsCount;
    private long affectedPassengers;
    private double avgVisibility;

    public AccumulateReportDTO() {}

    public AccumulateReportDTO(long totalDelayMinutes, long delayedFlightsCount,
                                long affectedPassengers, double avgVisibility) {
        this.totalDelayMinutes = totalDelayMinutes;
        this.delayedFlightsCount = delayedFlightsCount;
        this.affectedPassengers = affectedPassengers;
        this.avgVisibility = avgVisibility;
    }

    public long getTotalDelayMinutes() { return totalDelayMinutes; }
    public void setTotalDelayMinutes(long totalDelayMinutes) { this.totalDelayMinutes = totalDelayMinutes; }

    public long getDelayedFlightsCount() { return delayedFlightsCount; }
    public void setDelayedFlightsCount(long delayedFlightsCount) { this.delayedFlightsCount = delayedFlightsCount; }

    public long getAffectedPassengers() { return affectedPassengers; }
    public void setAffectedPassengers(long affectedPassengers) { this.affectedPassengers = affectedPassengers; }

    public double getAvgVisibility() { return avgVisibility; }
    public void setAvgVisibility(double avgVisibility) { this.avgVisibility = avgVisibility; }

    @Override
    public String toString() {
        return "AccumulateReportDTO{" +
                "totalDelayMinutes=" + totalDelayMinutes +
                ", delayedFlightsCount=" + delayedFlightsCount +
                ", affectedPassengers=" + affectedPassengers +
                ", avgVisibility=" + avgVisibility +
                '}';
    }
}
