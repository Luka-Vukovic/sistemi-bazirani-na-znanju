package com.ftn.sbnz.model.events;

import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Role(Role.Type.EVENT)
@Timestamp("timestamp")
public class WeatherUpdateEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private int flightNumber;
    private long timestamp;
    private int windSpeed;
    private int visibility;
    private int temperature;

    public WeatherUpdateEvent() {}

    public WeatherUpdateEvent(int flightNumber, int windSpeed, int visibility, int temperature) {
        this.flightNumber = flightNumber;
        this.timestamp = System.currentTimeMillis();
        this.windSpeed = windSpeed;
        this.visibility = visibility;
        this.temperature = temperature;
    }

    public int getFlightNumber() { return flightNumber; }
    public void setFlightNumber(int flightNumber) { this.flightNumber = flightNumber; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public int getWindSpeed() { return windSpeed; }
    public void setWindSpeed(int windSpeed) { this.windSpeed = windSpeed; }

    public int getVisibility() { return visibility; }
    public void setVisibility(int visibility) { this.visibility = visibility; }

    public int getTemperature() { return temperature; }
    public void setTemperature(int temperature) { this.temperature = temperature; }
}