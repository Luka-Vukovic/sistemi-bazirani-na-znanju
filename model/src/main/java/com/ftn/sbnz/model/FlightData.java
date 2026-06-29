package com.ftn.sbnz.model;

/**
 * Wrapper koji grupiše Flight sa odgovarajućim WeatherReport-om.
 * Koristi se za prosleđivanje podataka u accumulate sesiju.
 */
public class FlightData {

    private Flight flight;
    private WeatherReport weather;

    public FlightData() {}

    public FlightData(Flight flight, WeatherReport weather) {
        this.flight = flight;
        this.weather = weather;
    }

    public Flight getFlight() { return flight; }
    public void setFlight(Flight flight) { this.flight = flight; }

    public WeatherReport getWeather() { return weather; }
    public void setWeather(WeatherReport weather) { this.weather = weather; }
}
