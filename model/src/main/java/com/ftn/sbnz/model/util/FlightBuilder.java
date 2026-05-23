package com.ftn.sbnz.model.util;

import com.ftn.sbnz.model.Aircraft;
import com.ftn.sbnz.model.Flight;
import com.ftn.sbnz.model.enums.FlightCategory;
import com.ftn.sbnz.model.enums.FlightStatus;

import java.time.LocalDateTime;

public class FlightBuilder {

    private final Flight instance;

    public FlightBuilder() {
        this.instance = new Flight();
        // default vrednosti
        this.instance.setFlightNumber(0);
        this.instance.setRoute("");
        this.instance.setPlannedDeparture(LocalDateTime.now());
        this.instance.setPlannedArrival(LocalDateTime.now().plusHours(2));
        this.instance.setCategory(FlightCategory.SCHEDULED_COMMERCIAL);
        this.instance.setStatus(FlightStatus.ON_TIME);
        this.instance.setDelayMinutes(0);
        this.instance.setPassengerCount(0);
        this.instance.setHasReplacementAircraft(false);
        this.instance.setHasReplacementCrew(false);
    }

    public FlightBuilder withFlightNumber(int flightNumber) {
        this.instance.setFlightNumber(flightNumber);
        return this;
    }

    public FlightBuilder withRoute(String route) {
        this.instance.setRoute(route);
        return this;
    }

    public FlightBuilder withAircraft(Aircraft aircraft) {
        this.instance.setAircraft(aircraft);
        return this;
    }

    public FlightBuilder withPlannedDeparture(LocalDateTime plannedDeparture) {
        this.instance.setPlannedDeparture(plannedDeparture);
        return this;
    }

    public FlightBuilder withPlannedArrival(LocalDateTime plannedArrival) {
        this.instance.setPlannedArrival(plannedArrival);
        return this;
    }

    public FlightBuilder withCategory(FlightCategory category) {
        this.instance.setCategory(category);
        return this;
    }

    public FlightBuilder withStatus(FlightStatus status) {
        this.instance.setStatus(status);
        return this;
    }

    public FlightBuilder withDelayMinutes(int delayMinutes) {
        this.instance.setDelayMinutes(delayMinutes);
        return this;
    }

    public FlightBuilder withPassengerCount(int passengerCount) {
        this.instance.setPassengerCount(passengerCount);
        return this;
    }

    public FlightBuilder withReplacementAircraft(boolean hasReplacementAircraft) {
        this.instance.setHasReplacementAircraft(hasReplacementAircraft);
        return this;
    }

    public FlightBuilder withReplacementCrew(boolean hasReplacementCrew) {
        this.instance.setHasReplacementCrew(hasReplacementCrew);
        return this;
    }

    public Flight build() {
        return this.instance;
    }
}
