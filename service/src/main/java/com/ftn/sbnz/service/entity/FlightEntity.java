package com.ftn.sbnz.service.entity;

import com.ftn.sbnz.model.enums.FlightCategory;
import com.ftn.sbnz.model.enums.FlightStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "flight")
public class FlightEntity {

    @Id
    private int flightNumber;

    private String route;
    private FlightCategory category;
    private FlightStatus status;
    private LocalDateTime plannedDeparture;
    private LocalDateTime plannedArrival;
    private int passengerCount;
    private int delayMinutes;
    private boolean hasReplacementAircraft;
    private boolean hasReplacementCrew;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "aircraft_id")
    private AircraftEntity aircraft;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "weather_id")
    private WeatherReportEntity weather;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "runway_id")
    private RunwayEntity runway;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "airport_id")
    private AirportEntity airport;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "crew_id")
    private CrewEntity crew;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "flight_number", referencedColumnName = "flightNumber")
    private java.util.List<TechnicalAlarmEntity> alarms = new java.util.ArrayList<>();

    public FlightEntity() {}

    public int getFlightNumber() { return flightNumber; }
    public void setFlightNumber(int v) { this.flightNumber = v; }
    public String getRoute() { return route; }
    public void setRoute(String v) { this.route = v; }
    public FlightCategory getCategory() { return category; }
    public void setCategory(FlightCategory v) { this.category = v; }
    public FlightStatus getStatus() { return status; }
    public void setStatus(FlightStatus v) { this.status = v; }
    public LocalDateTime getPlannedDeparture() { return plannedDeparture; }
    public void setPlannedDeparture(LocalDateTime v) { this.plannedDeparture = v; }
    public LocalDateTime getPlannedArrival() { return plannedArrival; }
    public void setPlannedArrival(LocalDateTime v) { this.plannedArrival = v; }
    public int getPassengerCount() { return passengerCount; }
    public void setPassengerCount(int v) { this.passengerCount = v; }
    public int getDelayMinutes() { return delayMinutes; }
    public void setDelayMinutes(int v) { this.delayMinutes = v; }
    public boolean isHasReplacementAircraft() { return hasReplacementAircraft; }
    public void setHasReplacementAircraft(boolean v) { this.hasReplacementAircraft = v; }
    public boolean isHasReplacementCrew() { return hasReplacementCrew; }
    public void setHasReplacementCrew(boolean v) { this.hasReplacementCrew = v; }
    public AircraftEntity getAircraft() { return aircraft; }
    public void setAircraft(AircraftEntity v) { this.aircraft = v; }
    public WeatherReportEntity getWeather() { return weather; }
    public void setWeather(WeatherReportEntity v) { this.weather = v; }
    public RunwayEntity getRunway() { return runway; }
    public void setRunway(RunwayEntity v) { this.runway = v; }
    public AirportEntity getAirport() { return airport; }
    public void setAirport(AirportEntity v) { this.airport = v; }
    public CrewEntity getCrew() { return crew; }
    public void setCrew(CrewEntity v) { this.crew = v; }
    public java.util.List<TechnicalAlarmEntity> getAlarms() { return alarms; }
    public void setAlarms(java.util.List<TechnicalAlarmEntity> v) { this.alarms = v; }
}
