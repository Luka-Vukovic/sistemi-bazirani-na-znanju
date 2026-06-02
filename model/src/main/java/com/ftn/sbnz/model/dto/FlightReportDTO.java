package com.ftn.sbnz.model.dto;

import com.ftn.sbnz.model.Flight;
import com.ftn.sbnz.model.WeatherReport;
import com.ftn.sbnz.model.Runway;
import com.ftn.sbnz.model.Recommendation;

import java.io.Serializable;
import java.util.List;

public class FlightReportDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Flight flight;
    private WeatherReport weather;
    private Runway runway;
    private Recommendation recommendation;
    private List<String> unmetConditions;

    public FlightReportDTO() {}

    public FlightReportDTO(Flight flight, WeatherReport weather, Runway runway,
                           Recommendation recommendation) {
        this.flight = flight;
        this.weather = weather;
        this.runway = runway;
        this.recommendation = recommendation;
    }

    public FlightReportDTO(Flight flight, WeatherReport weather, Runway runway,
                           Recommendation recommendation, List<String> unmetConditions) {
        this.flight = flight;
        this.weather = weather;
        this.runway = runway;
        this.recommendation = recommendation;
        this.unmetConditions = unmetConditions;
    }

    public Flight getFlight() { return flight; }
    public void setFlight(Flight flight) { this.flight = flight; }

    public WeatherReport getWeather() { return weather; }
    public void setWeather(WeatherReport weather) { this.weather = weather; }

    public Runway getRunway() { return runway; }
    public void setRunway(Runway runway) { this.runway = runway; }

    public Recommendation getRecommendation() { return recommendation; }
    public void setRecommendation(Recommendation recommendation) { this.recommendation = recommendation; }

    public List<String> getUnmetConditions() { return unmetConditions; }
    public void setUnmetConditions(List<String> unmetConditions) { this.unmetConditions = unmetConditions; }
}