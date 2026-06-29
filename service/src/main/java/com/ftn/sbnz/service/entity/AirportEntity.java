package com.ftn.sbnz.service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "airport")
public class AirportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int freeGates;
    private int capacity;
    private int totalRunways;
    private int availableRunways;
    private int runwayHeading;
    private int runwayLength;
    private boolean lvtoCapability;
    private boolean lvtoPermit;
    private boolean specialPermit;

    public AirportEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getFreeGates() { return freeGates; }
    public void setFreeGates(int v) { this.freeGates = v; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int v) { this.capacity = v; }
    public int getTotalRunways() { return totalRunways; }
    public void setTotalRunways(int v) { this.totalRunways = v; }
    public int getAvailableRunways() { return availableRunways; }
    public void setAvailableRunways(int v) { this.availableRunways = v; }
    public int getRunwayHeading() { return runwayHeading; }
    public void setRunwayHeading(int v) { this.runwayHeading = v; }
    public int getRunwayLength() { return runwayLength; }
    public void setRunwayLength(int v) { this.runwayLength = v; }
    public boolean isLvtoCapability() { return lvtoCapability; }
    public void setLvtoCapability(boolean v) { this.lvtoCapability = v; }
    public boolean isLvtoPermit() { return lvtoPermit; }
    public void setLvtoPermit(boolean v) { this.lvtoPermit = v; }
    public boolean isSpecialPermit() { return specialPermit; }
    public void setSpecialPermit(boolean v) { this.specialPermit = v; }
}
