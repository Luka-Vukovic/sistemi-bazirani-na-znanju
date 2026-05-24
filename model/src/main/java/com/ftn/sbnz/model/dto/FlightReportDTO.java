package com.ftn.sbnz.model.dto;

import com.ftn.sbnz.model.Flight;
import com.ftn.sbnz.model.WeatherReport;
import com.ftn.sbnz.model.Runway;
import com.ftn.sbnz.model.Recommendation;

import java.io.Serializable;

public class FlightReportDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Flight flight;
    private WeatherReport weather;
    private Runway runway;
    private Recommendation recommendation;

    public FlightReportDTO() {}

    public FlightReportDTO(Flight flight, WeatherReport weather, Runway runway, Recommendation recommendation) {
        this.flight = flight;
        this.weather = weather;
        this.runway = runway;
        this.recommendation = recommendation;
    }

    public Flight getFlight() { return flight; }
    public void setFlight(Flight flight) { this.flight = flight; }

    public WeatherReport getWeather() { return weather; }
    public void setWeather(WeatherReport weather) { this.weather = weather; }

    public Runway getRunway() { return runway; }
    public void setRunway(Runway runway) { this.runway = runway; }

    public Recommendation getRecommendation() { return recommendation; }
    public void setRecommendation(Recommendation recommendation) { this.recommendation = recommendation; }
}