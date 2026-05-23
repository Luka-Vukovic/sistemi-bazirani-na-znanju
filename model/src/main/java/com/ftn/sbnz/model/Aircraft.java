package com.ftn.sbnz.model;

import com.ftn.sbnz.model.enums.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Aircraft implements Serializable {

    private static final long serialVersionUID = 1L;

    private int age;
    private LocalDate nextServiceDate;
    private int flightHoursSinceService;
    private int cyclesSinceService;
    private int totalFlightHours;

    public Aircraft() {}

    public Aircraft(int age, LocalDate nextServiceDate, int flightHoursSinceService,
                    int cyclesSinceService, int totalFlightHours) {
        this.age = age;
        this.nextServiceDate = nextServiceDate;
        this.flightHoursSinceService = flightHoursSinceService;
        this.cyclesSinceService = cyclesSinceService;
        this.totalFlightHours = totalFlightHours;
    }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public LocalDate getNextServiceDate() { return nextServiceDate; }
    public void setNextServiceDate(LocalDate nextServiceDate) { this.nextServiceDate = nextServiceDate; }

    public int getFlightHoursSinceService() { return flightHoursSinceService; }
    public void setFlightHoursSinceService(int flightHoursSinceService) { this.flightHoursSinceService = flightHoursSinceService; }

    public int getCyclesSinceService() { return cyclesSinceService; }
    public void setCyclesSinceService(int cyclesSinceService) { this.cyclesSinceService = cyclesSinceService; }

    public int getTotalFlightHours() { return totalFlightHours; }
    public void setTotalFlightHours(int totalFlightHours) { this.totalFlightHours = totalFlightHours; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aircraft aircraft = (Aircraft) o;
        return age == aircraft.age &&
                flightHoursSinceService == aircraft.flightHoursSinceService &&
                cyclesSinceService == aircraft.cyclesSinceService &&
                totalFlightHours == aircraft.totalFlightHours &&
                Objects.equals(nextServiceDate, aircraft.nextServiceDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(age, nextServiceDate, flightHoursSinceService, cyclesSinceService, totalFlightHours);
    }

    @Override
    public String toString() {
        return "Aircraft{" +
                "age=" + age +
                ", nextServiceDate=" + nextServiceDate +
                ", flightHoursSinceService=" + flightHoursSinceService +
                ", cyclesSinceService=" + cyclesSinceService +
                ", totalFlightHours=" + totalFlightHours +
                '}';
    }
}
