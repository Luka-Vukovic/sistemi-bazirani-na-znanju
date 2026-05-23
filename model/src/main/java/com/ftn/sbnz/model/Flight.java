package com.ftn.sbnz.model;

import com.ftn.sbnz.model.enums.FlightCategory;
import com.ftn.sbnz.model.enums.FlightStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Flight implements Serializable {

    private static final long serialVersionUID = 1L;

    private int flightNumber;
    private String route;
    private Aircraft aircraft;
    private LocalDateTime plannedDeparture;
    private LocalDateTime plannedArrival;
    private FlightCategory category;
    private FlightStatus status;
    private int delayMinutes;
    private int passengerCount;
    private boolean hasReplacementAircraft;
    private boolean hasReplacementCrew;

    public Flight() {}

    public Flight(int flightNumber, String route, Aircraft aircraft,
                  LocalDateTime plannedDeparture, LocalDateTime plannedArrival,
                  FlightCategory category, FlightStatus status, int delayMinutes,
                  int passengerCount, boolean hasReplacementAircraft, boolean hasReplacementCrew) {
        this.flightNumber = flightNumber;
        this.route = route;
        this.aircraft = aircraft;
        this.plannedDeparture = plannedDeparture;
        this.plannedArrival = plannedArrival;
        this.category = category;
        this.status = status;
        this.delayMinutes = delayMinutes;
        this.passengerCount = passengerCount;
        this.hasReplacementAircraft = hasReplacementAircraft;
        this.hasReplacementCrew = hasReplacementCrew;
    }

    public int getFlightNumber() { return flightNumber; }
    public void setFlightNumber(int flightNumber) { this.flightNumber = flightNumber; }

    public String getRoute() { return route; }
    public void setRoute(String route) { this.route = route; }

    public Aircraft getAircraft() { return aircraft; }
    public void setAircraft(Aircraft aircraft) { this.aircraft = aircraft; }

    public LocalDateTime getPlannedDeparture() { return plannedDeparture; }
    public void setPlannedDeparture(LocalDateTime plannedDeparture) { this.plannedDeparture = plannedDeparture; }

    public LocalDateTime getPlannedArrival() { return plannedArrival; }
    public void setPlannedArrival(LocalDateTime plannedArrival) { this.plannedArrival = plannedArrival; }

    public FlightCategory getCategory() { return category; }
    public void setCategory(FlightCategory category) { this.category = category; }

    public FlightStatus getStatus() { return status; }
    public void setStatus(FlightStatus status) { this.status = status; }

    public int getDelayMinutes() { return delayMinutes; }
    public void setDelayMinutes(int delayMinutes) { this.delayMinutes = delayMinutes; }

    public int getPassengerCount() { return passengerCount; }
    public void setPassengerCount(int passengerCount) { this.passengerCount = passengerCount; }

    public boolean isHasReplacementAircraft() { return hasReplacementAircraft; }
    public void setHasReplacementAircraft(boolean hasReplacementAircraft) { this.hasReplacementAircraft = hasReplacementAircraft; }

    public boolean isHasReplacementCrew() { return hasReplacementCrew; }
    public void setHasReplacementCrew(boolean hasReplacementCrew) { this.hasReplacementCrew = hasReplacementCrew; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return flightNumber == flight.flightNumber &&
                delayMinutes == flight.delayMinutes &&
                passengerCount == flight.passengerCount &&
                hasReplacementAircraft == flight.hasReplacementAircraft &&
                hasReplacementCrew == flight.hasReplacementCrew &&
                Objects.equals(route, flight.route) &&
                Objects.equals(aircraft, flight.aircraft) &&
                Objects.equals(plannedDeparture, flight.plannedDeparture) &&
                Objects.equals(plannedArrival, flight.plannedArrival) &&
                category == flight.category &&
                status == flight.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(flightNumber, route, aircraft, plannedDeparture, plannedArrival,
                category, status, delayMinutes, passengerCount, hasReplacementAircraft, hasReplacementCrew);
    }

    @Override
    public String toString() {
        return "Flight{" +
                "flightNumber=" + flightNumber +
                ", route='" + route + '\'' +
                ", aircraft=" + aircraft +
                ", plannedDeparture=" + plannedDeparture +
                ", plannedArrival=" + plannedArrival +
                ", category=" + category +
                ", status=" + status +
                ", delayMinutes=" + delayMinutes +
                ", passengerCount=" + passengerCount +
                ", hasReplacementAircraft=" + hasReplacementAircraft +
                ", hasReplacementCrew=" + hasReplacementCrew +
                '}';
    }
}
