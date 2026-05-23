package com.ftn.sbnz.model.util;

import com.ftn.sbnz.model.WeatherReport;
import com.ftn.sbnz.model.enums.PrecipitationIntensity;
import com.ftn.sbnz.model.enums.PrecipitationType;

public class WeatherReportBuilder {

    private final WeatherReport instance;

    public WeatherReportBuilder() {
        this.instance = new WeatherReport();
        // default vrednosti — idealno vreme
        this.instance.setWindSpeed(0);
        this.instance.setWindDirection(0);
        this.instance.setCrosswind(0);
        this.instance.setTailwind(0);
        this.instance.setVisibility(10000);
        this.instance.setTemperature(15);
        this.instance.setDewPoint(5);
        this.instance.setPrecipitationType(PrecipitationType.DRY);
        this.instance.setPrecipitationIntensity(PrecipitationIntensity.NONE);
        this.instance.setIcingPresent(false);
    }

    public WeatherReportBuilder withWindSpeed(int windSpeed) {
        this.instance.setWindSpeed(windSpeed);
        return this;
    }

    public WeatherReportBuilder withWindDirection(int windDirection) {
        this.instance.setWindDirection(windDirection);
        return this;
    }

    public WeatherReportBuilder withCrosswind(int crosswind) {
        this.instance.setCrosswind(crosswind);
        return this;
    }

    public WeatherReportBuilder withTailwind(int tailwind) {
        this.instance.setTailwind(tailwind);
        return this;
    }

    public WeatherReportBuilder withVisibility(int visibility) {
        this.instance.setVisibility(visibility);
        return this;
    }

    public WeatherReportBuilder withTemperature(int temperature) {
        this.instance.setTemperature(temperature);
        return this;
    }

    public WeatherReportBuilder withDewPoint(int dewPoint) {
        this.instance.setDewPoint(dewPoint);
        return this;
    }

    public WeatherReportBuilder withPrecipitationType(PrecipitationType type) {
        this.instance.setPrecipitationType(type);
        return this;
    }

    public WeatherReportBuilder withPrecipitationIntensity(PrecipitationIntensity intensity) {
        this.instance.setPrecipitationIntensity(intensity);
        return this;
    }

    public WeatherReportBuilder withIcingPresent(boolean icingPresent) {
        this.instance.setIcingPresent(icingPresent);
        return this;
    }

    public WeatherReport build() {
        return this.instance;
    }
}
