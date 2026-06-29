package com.ftn.sbnz.service.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "aircraft")
public class AircraftEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int age;
    private LocalDate nextServiceDate;
    private int flightHoursSinceService;
    private int cyclesSinceService;
    private int totalFlightHours;

    public AircraftEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public LocalDate getNextServiceDate() { return nextServiceDate; }
    public void setNextServiceDate(LocalDate nextServiceDate) { this.nextServiceDate = nextServiceDate; }
    public int getFlightHoursSinceService() { return flightHoursSinceService; }
    public void setFlightHoursSinceService(int v) { this.flightHoursSinceService = v; }
    public int getCyclesSinceService() { return cyclesSinceService; }
    public void setCyclesSinceService(int v) { this.cyclesSinceService = v; }
    public int getTotalFlightHours() { return totalFlightHours; }
    public void setTotalFlightHours(int v) { this.totalFlightHours = v; }
}
