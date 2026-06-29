package com.ftn.sbnz.model;

import java.io.Serializable;

public class AccumulateResult implements Serializable {

    private static final long serialVersionUID = 1L;

    // Accumulate 1: ukupno kašnjenje i broj pogođenih letova danas
    private long totalDelayMinutes;
    private long delayedFlightsCount;

    // Accumulate 2: ukupan broj pogođenih putnika
    private long affectedPassengers;

    // Accumulate 3: prosečna vidljivost iz WeatherReport-ova u sesiji
    private double avgVisibility;

    public AccumulateResult() {}

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
        return "AccumulateResult{" +
                "totalDelayMinutes=" + totalDelayMinutes +
                ", delayedFlightsCount=" + delayedFlightsCount +
                ", affectedPassengers=" + affectedPassengers +
                ", avgVisibility=" + avgVisibility +
                '}';
    }
}
