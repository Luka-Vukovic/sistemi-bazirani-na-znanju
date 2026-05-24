package com.ftn.sbnz.model.util;

import com.ftn.sbnz.model.*;
import com.ftn.sbnz.model.enums.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Fabrika test scenarija za Drools sesije.
 * Svaki metod vraća kompletan skup objekata za jedan scenario.
 */
public class ScenarioFactory {

    /**
     * Scenario JU-301: Beograd -> London
     * Loši vremenski uslovi, RWYCC=3, posada umorna, 3 SREDNJA alarma
     * Očekivana preporuka: OTKAŽI
     */
    public static Flight getFlightJU301() {
        Aircraft aircraft = new AircraftBuilder()
                .withAge(10)
                .withNextServiceDate(LocalDate.now().plusMonths(3))
                .withFlightHoursSinceService(400)
                .withCyclesSinceService(180)
                .withTotalFlightHours(15000)
                .build();

        return new FlightBuilder()
                .withFlightNumber(301)
                .withRoute("BEG-LHR")
                .withAircraft(aircraft)
                .withPlannedDeparture(LocalDateTime.now().withHour(14).withMinute(0))
                .withPlannedArrival(LocalDateTime.now().withHour(16).withMinute(30))
                .withCategory(FlightCategory.SCHEDULED_COMMERCIAL)
                .withStatus(FlightStatus.DELAYED)
                .withPassengerCount(180)
                .withReplacementAircraft(false)
                .withReplacementCrew(false)
                .build();
    }

    public static WeatherReport getBadWeatherJU301() {
        return new WeatherReportBuilder()
                .withCrosswind(60)
                .withTailwind(22)
                .withVisibility(380)
                .withTemperature(-3)
                .withDewPoint(-3)
                .withPrecipitationType(PrecipitationType.RAIN)
                .withPrecipitationIntensity(PrecipitationIntensity.MODERATE)
                .withIcingPresent(false)
                .build();
    }

    public static Runway getBadRunwayJU301() {
        return new Runway(RunwayStatus.OPEN, 3, false, false);
    }

    public static Crew getTiredCrewJU301() {
        return new Crew(1, true, 12, 10, 3, false);
    }

    public static Airport getBelgradeAirport() {
        return new Airport(5, 100, 2, 2, 120, 3400,
                false, false, false);
    }

    /**
     * Scenario normalnog leta — sve u redu
     * Očekivana preporuka: POLETI NA VREME
     */
    public static Flight getNormalFlight() {
        Aircraft aircraft = new AircraftBuilder()
                .withAge(5)
                .withNextServiceDate(LocalDate.now().plusMonths(6))
                .withFlightHoursSinceService(200)
                .withCyclesSinceService(100)
                .withTotalFlightHours(8000)
                .build();

        return new FlightBuilder()
                .withFlightNumber(100)
                .withRoute("BEG-VIE")
                .withAircraft(aircraft)
                .withPlannedDeparture(LocalDateTime.now().plusHours(1))
                .withCategory(FlightCategory.SCHEDULED_COMMERCIAL)
                .withStatus(FlightStatus.ON_TIME)
                .withPassengerCount(150)
                .withReplacementAircraft(true)
                .withReplacementCrew(true)
                .build();
    }

    public static WeatherReport getGoodWeather() {
        return new WeatherReportBuilder()
                .withCrosswind(10)
                .withTailwind(5)
                .withVisibility(8000)
                .withTemperature(15)
                .withDewPoint(5)
                .withPrecipitationType(PrecipitationType.DRY)
                .withPrecipitationIntensity(PrecipitationIntensity.NONE)
                .withIcingPresent(false)
                .build();
    }

    public static Runway getGoodRunway() {
        return new Runway(RunwayStatus.OPEN, 6, false, false);
    }

    public static Crew getRestedCrew() {
        return new Crew(1, true, 4, 14, 1, false);
    }
}
