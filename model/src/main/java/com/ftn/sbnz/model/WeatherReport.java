package com.ftn.sbnz.model;

import com.ftn.sbnz.model.enums.PrecipitationIntensity;
import com.ftn.sbnz.model.enums.PrecipitationType;

import java.io.Serializable;
import java.util.Objects;

public class WeatherReport implements Serializable {

    private static final long serialVersionUID = 1L;

    private int windSpeed;
    private int windDirection;
    private int crosswind;
    private int tailwind;
    private int visibility;
    private int temperature;
    private int dewPoint;
    private PrecipitationType precipitationType;
    private PrecipitationIntensity precipitationIntensity;
    private boolean icingPresent;

    public WeatherReport() {}

    public WeatherReport(int windSpeed, int windDirection, int crosswind, int tailwind,
                         int visibility, int temperature, int dewPoint,
                         PrecipitationType precipitationType,
                         PrecipitationIntensity precipitationIntensity,
                         boolean icingPresent) {
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.crosswind = crosswind;
        this.tailwind = tailwind;
        this.visibility = visibility;
        this.temperature = temperature;
        this.dewPoint = dewPoint;
        this.precipitationType = precipitationType;
        this.precipitationIntensity = precipitationIntensity;
        this.icingPresent = icingPresent;
    }

    public int getWindSpeed() { return windSpeed; }
    public void setWindSpeed(int windSpeed) { this.windSpeed = windSpeed; }

    public int getWindDirection() { return windDirection; }
    public void setWindDirection(int windDirection) { this.windDirection = windDirection; }

    public int getCrosswind() { return crosswind; }
    public void setCrosswind(int crosswind) { this.crosswind = crosswind; }

    public int getTailwind() { return tailwind; }
    public void setTailwind(int tailwind) { this.tailwind = tailwind; }

    public int getVisibility() { return visibility; }
    public void setVisibility(int visibility) { this.visibility = visibility; }

    public int getTemperature() { return temperature; }
    public void setTemperature(int temperature) { this.temperature = temperature; }

    public int getDewPoint() { return dewPoint; }
    public void setDewPoint(int dewPoint) { this.dewPoint = dewPoint; }

    public PrecipitationType getPrecipitationType() { return precipitationType; }
    public void setPrecipitationType(PrecipitationType precipitationType) { this.precipitationType = precipitationType; }

    public PrecipitationIntensity getPrecipitationIntensity() { return precipitationIntensity; }
    public void setPrecipitationIntensity(PrecipitationIntensity precipitationIntensity) { this.precipitationIntensity = precipitationIntensity; }

    public boolean isIcingPresent() { return icingPresent; }
    public void setIcingPresent(boolean icingPresent) { this.icingPresent = icingPresent; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeatherReport that = (WeatherReport) o;
        return windSpeed == that.windSpeed &&
                windDirection == that.windDirection &&
                crosswind == that.crosswind &&
                tailwind == that.tailwind &&
                visibility == that.visibility &&
                temperature == that.temperature &&
                dewPoint == that.dewPoint &&
                icingPresent == that.icingPresent &&
                precipitationType == that.precipitationType &&
                precipitationIntensity == that.precipitationIntensity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(windSpeed, windDirection, crosswind, tailwind, visibility,
                temperature, dewPoint, precipitationType, precipitationIntensity, icingPresent);
    }

    @Override
    public String toString() {
        return "WeatherReport{" +
                "windSpeed=" + windSpeed +
                ", windDirection=" + windDirection +
                ", crosswind=" + crosswind +
                ", tailwind=" + tailwind +
                ", visibility=" + visibility +
                ", temperature=" + temperature +
                ", dewPoint=" + dewPoint +
                ", precipitationType=" + precipitationType +
                ", precipitationIntensity=" + precipitationIntensity +
                ", icingPresent=" + icingPresent +
                '}';
    }
}
