package com.ftn.sbnz.service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "crew")
public class CrewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int flightNumber;
    private boolean complete;
    private double fdp;
    private double restBeforeFlight;
    private int sectorsToday;
    private boolean nightDuty;

    public CrewEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getFlightNumber() { return flightNumber; }
    public void setFlightNumber(int v) { this.flightNumber = v; }
    public boolean isComplete() { return complete; }
    public void setComplete(boolean v) { this.complete = v; }
    public double getFdp() { return fdp; }
    public void setFdp(double v) { this.fdp = v; }
    public double getRestBeforeFlight() { return restBeforeFlight; }
    public void setRestBeforeFlight(double v) { this.restBeforeFlight = v; }
    public int getSectorsToday() { return sectorsToday; }
    public void setSectorsToday(int v) { this.sectorsToday = v; }
    public boolean isNightDuty() { return nightDuty; }
    public void setNightDuty(boolean v) { this.nightDuty = v; }
}
