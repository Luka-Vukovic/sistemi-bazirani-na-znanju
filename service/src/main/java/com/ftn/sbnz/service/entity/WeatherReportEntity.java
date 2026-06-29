package com.ftn.sbnz.service.entity;

import com.ftn.sbnz.model.enums.PrecipitationIntensity;
import com.ftn.sbnz.model.enums.PrecipitationType;
import jakarta.persistence.*;

@Entity
@Table(name = "weather_report")
public class WeatherReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int windSpeed;
    private int windDirection;
    private int crosswind;
    private int tailwind;
    private int visibility;
    private int temperature;
    private int dewPoint;

    @Enumerated(EnumType.STRING)
    private PrecipitationType precipitationType;

    @Enumerated(EnumType.STRING)
    private PrecipitationIntensity precipitationIntensity;

    private boolean icingPresent;

    public WeatherReportEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getWindSpeed() { return windSpeed; }
    public void setWindSpeed(int v) { this.windSpeed = v; }
    public int getWindDirection() { return windDirection; }
    public void setWindDirection(int v) { this.windDirection = v; }
    public int getCrosswind() { return crosswind; }
    public void setCrosswind(int v) { this.crosswind = v; }
    public int getTailwind() { return tailwind; }
    public void setTailwind(int v) { this.tailwind = v; }
    public int getVisibility() { return visibility; }
    public void setVisibility(int v) { this.visibility = v; }
    public int getTemperature() { return temperature; }
    public void setTemperature(int v) { this.temperature = v; }
    public int getDewPoint() { return dewPoint; }
    public void setDewPoint(int v) { this.dewPoint = v; }
    public PrecipitationType getPrecipitationType() { return precipitationType; }
    public void setPrecipitationType(PrecipitationType v) { this.precipitationType = v; }
    public PrecipitationIntensity getPrecipitationIntensity() { return precipitationIntensity; }
    public void setPrecipitationIntensity(PrecipitationIntensity v) { this.precipitationIntensity = v; }
    public boolean isIcingPresent() { return icingPresent; }
    public void setIcingPresent(boolean v) { this.icingPresent = v; }
}
