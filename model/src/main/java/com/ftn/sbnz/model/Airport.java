package com.ftn.sbnz.model;

import java.io.Serializable;
import java.util.Objects;

public class Airport implements Serializable {

    private static final long serialVersionUID = 1L;

    private int freeGates;
    private int capacity;
    private int totalRunways;
    private int availableRunways;
    private int runwayHeading;
    private int runwayLength;
    private boolean lvtoCapability;
    private boolean lvtoPermit;
    private boolean specialPermit;

    public Airport() {}

    public Airport(int freeGates, int capacity, int totalRunways, int availableRunways,
                   int runwayHeading, int runwayLength, boolean lvtoCapability,
                   boolean lvtoPermit, boolean specialPermit) {
        this.freeGates = freeGates;
        this.capacity = capacity;
        this.totalRunways = totalRunways;
        this.availableRunways = availableRunways;
        this.runwayHeading = runwayHeading;
        this.runwayLength = runwayLength;
        this.lvtoCapability = lvtoCapability;
        this.lvtoPermit = lvtoPermit;
        this.specialPermit = specialPermit;
    }

    public int getFreeGates() { return freeGates; }
    public void setFreeGates(int freeGates) { this.freeGates = freeGates; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public int getTotalRunways() { return totalRunways; }
    public void setTotalRunways(int totalRunways) { this.totalRunways = totalRunways; }

    public int getAvailableRunways() { return availableRunways; }
    public void setAvailableRunways(int availableRunways) { this.availableRunways = availableRunways; }

    public int getRunwayHeading() { return runwayHeading; }
    public void setRunwayHeading(int runwayHeading) { this.runwayHeading = runwayHeading; }

    public int getRunwayLength() { return runwayLength; }
    public void setRunwayLength(int runwayLength) { this.runwayLength = runwayLength; }

    public boolean isLvtoCapability() { return lvtoCapability; }
    public void setLvtoCapability(boolean lvtoCapability) { this.lvtoCapability = lvtoCapability; }

    public boolean isLvtoPermit() { return lvtoPermit; }
    public void setLvtoPermit(boolean lvtoPermit) { this.lvtoPermit = lvtoPermit; }

    public boolean isSpecialPermit() { return specialPermit; }
    public void setSpecialPermit(boolean specialPermit) { this.specialPermit = specialPermit; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport = (Airport) o;
        return freeGates == airport.freeGates &&
                capacity == airport.capacity &&
                totalRunways == airport.totalRunways &&
                availableRunways == airport.availableRunways &&
                runwayHeading == airport.runwayHeading &&
                runwayLength == airport.runwayLength &&
                lvtoCapability == airport.lvtoCapability &&
                lvtoPermit == airport.lvtoPermit &&
                specialPermit == airport.specialPermit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(freeGates, capacity, totalRunways, availableRunways,
                runwayHeading, runwayLength, lvtoCapability, lvtoPermit, specialPermit);
    }

    @Override
    public String toString() {
        return "Airport{" +
                "freeGates=" + freeGates +
                ", capacity=" + capacity +
                ", totalRunways=" + totalRunways +
                ", availableRunways=" + availableRunways +
                ", runwayHeading=" + runwayHeading +
                ", runwayLength=" + runwayLength +
                ", lvtoCapability=" + lvtoCapability +
                ", lvtoPermit=" + lvtoPermit +
                ", specialPermit=" + specialPermit +
                '}';
    }
}
