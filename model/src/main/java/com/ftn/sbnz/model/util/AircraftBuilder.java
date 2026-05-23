package com.ftn.sbnz.model.util;

import com.ftn.sbnz.model.Aircraft;

import java.time.LocalDate;

public class AircraftBuilder {

    private final Aircraft instance;

    public AircraftBuilder() {
        this.instance = new Aircraft();
        // default vrednosti
        this.instance.setAge(0);
        this.instance.setNextServiceDate(LocalDate.now().plusMonths(6));
        this.instance.setFlightHoursSinceService(0);
        this.instance.setCyclesSinceService(0);
        this.instance.setTotalFlightHours(0);
    }

    public AircraftBuilder withAge(int age) {
        this.instance.setAge(age);
        return this;
    }

    public AircraftBuilder withNextServiceDate(LocalDate nextServiceDate) {
        this.instance.setNextServiceDate(nextServiceDate);
        return this;
    }

    public AircraftBuilder withFlightHoursSinceService(int hours) {
        this.instance.setFlightHoursSinceService(hours);
        return this;
    }

    public AircraftBuilder withCyclesSinceService(int cycles) {
        this.instance.setCyclesSinceService(cycles);
        return this;
    }

    public AircraftBuilder withTotalFlightHours(int totalHours) {
        this.instance.setTotalFlightHours(totalHours);
        return this;
    }

    public Aircraft build() {
        return this.instance;
    }
}
