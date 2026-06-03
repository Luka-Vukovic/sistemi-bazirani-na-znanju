package com.ftn.sbnz.service.demo;

import com.ftn.sbnz.model.*;
import com.ftn.sbnz.model.enums.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DroolsConsoleDemo implements CommandLineRunner {

    public static final Map<Integer, Flight> flightsRepository = new HashMap<>();
    public static final Map<Integer, WeatherReport> weatherRepository = new HashMap<>();
    public static final Map<Integer, Runway> runwayRepository = new HashMap<>();
    public static final Map<Integer, Airport> airportRepository = new HashMap<>();
    public static final Map<Integer, Crew> crewRepository = new HashMap<>();
    public static final Map<Integer, List<TechnicalAlarm>> alarmsRepository = new HashMap<>();

    @Override
    public void run(String... args) throws Exception {
        System.out.println("==================================================");
        System.out.println("   INITIALIZING FLIGHTS IN REPOSITORY             ");
        System.out.println("==================================================\n");

        initFlight101_Cancel();
        initFlight202_Delay_Deicing();
        initFlight303_DepartOnTime();
        initFlight404_GeneralAviation();

        System.out.println("==================================================\n");
    }

    // -------------------------------------------------------
    // Let 101 — CANCEL
    // Razlozi: nulta vidljivost + kritična pista + posada prekoračila normu
    // -------------------------------------------------------
    private void initFlight101_Cancel() {
        Aircraft ac101 = new Aircraft();
        ac101.setAge(10);
        ac101.setNextServiceDate(LocalDate.now().plusDays(60));
        ac101.setFlightHoursSinceService(200);
        ac101.setCyclesSinceService(100);
        ac101.setTotalFlightHours(8000);

        Flight f101 = new Flight();
        f101.setFlightNumber(101);
        f101.setRoute("BEG-LHR");
        f101.setAircraft(ac101);
        f101.setCategory(FlightCategory.SCHEDULED_COMMERCIAL);
        f101.setStatus(FlightStatus.SCHEDULED);
        f101.setPlannedDeparture(LocalDateTime.now().plusHours(2));
        f101.setPlannedArrival(LocalDateTime.now().plusHours(4));
        f101.setPassengerCount(180);
        f101.setHasReplacementAircraft(false);
        f101.setHasReplacementCrew(false);

        WeatherReport w101 = new WeatherReport();
        w101.setWindSpeed(80);
        w101.setWindDirection(270);
        w101.setCrosswind(70);
        w101.setTailwind(25);
        w101.setVisibility(40);           // ZeroVisibility -> TakeoffForbidden
        w101.setTemperature(-5);
        w101.setDewPoint(-5);            // FrostRisk -> SurfacesContaminated
        w101.setPrecipitationType(PrecipitationType.RAIN);
        w101.setPrecipitationIntensity(PrecipitationIntensity.MODERATE);
        w101.setIcingPresent(false);

        Runway r101 = new Runway();
        r101.setStatus(RunwayStatus.OPEN);
        r101.setRwycc(1);                // RunwayCritical -> RunwayProblem
        r101.setRunwaysBeingDeiced(false);
        r101.setDeicingComplete(false);

        Airport a101 = new Airport();
        a101.setFreeGates(5);
        a101.setCapacity(50);
        a101.setTotalRunways(2);
        a101.setAvailableRunways(2);
        a101.setRunwayHeading(270);
        a101.setRunwayLength(3200);
        a101.setLvtoCapability(false);
        a101.setLvtoPermit(false);
        a101.setSpecialPermit(false);

        Crew c101 = new Crew();
        c101.setFlightNumber(101);
        c101.setComplete(true);
        c101.setFdp(14);                 // CrewExceededLimit (day, >13h)
        c101.setRestBeforeFlight(10);    // InsufficientRest
        c101.setSectorsToday(3);
        c101.setNightDuty(false);

        flightsRepository.put(101, f101);
        weatherRepository.put(101, w101);
        runwayRepository.put(101, r101);
        airportRepository.put(101, a101);
        crewRepository.put(101, c101);
        alarmsRepository.put(101, new ArrayList<>());

        System.out.println("Flight 101 initialized (expected: CANCEL)");
    }

    // -------------------------------------------------------
    // Let 202 — DELAY (de-icing)
    // Razlozi: temperatura -5, rosište -5, bez aktivnog leda
    // -------------------------------------------------------
    private void initFlight202_Delay_Deicing() {
        Aircraft ac202 = new Aircraft();
        ac202.setAge(8);
        ac202.setNextServiceDate(LocalDate.now().plusDays(90));
        ac202.setFlightHoursSinceService(300);
        ac202.setCyclesSinceService(150);
        ac202.setTotalFlightHours(5000);

        Flight f202 = new Flight();
        f202.setFlightNumber(202);
        f202.setRoute("BEG-CDG");
        f202.setAircraft(ac202);
        f202.setCategory(FlightCategory.SCHEDULED_COMMERCIAL);
        f202.setStatus(FlightStatus.SCHEDULED);
        f202.setPlannedDeparture(LocalDateTime.now().plusHours(4));
        f202.setPlannedArrival(LocalDateTime.now().plusHours(6));
        f202.setPassengerCount(150);
        f202.setHasReplacementAircraft(false);
        f202.setHasReplacementCrew(false);

        WeatherReport w202 = new WeatherReport();
        w202.setWindSpeed(15);
        w202.setWindDirection(180);
        w202.setCrosswind(10);
        w202.setTailwind(5);
        w202.setVisibility(3000);
        w202.setTemperature(-5);
        w202.setDewPoint(-5);            // FrostRisk -> SurfacesContaminated
        w202.setPrecipitationType(PrecipitationType.DRY);
        w202.setPrecipitationIntensity(PrecipitationIntensity.NONE);
        w202.setIcingPresent(false);

        Runway r202 = new Runway();
        r202.setStatus(RunwayStatus.OPEN);
        r202.setRwycc(4);
        r202.setRunwaysBeingDeiced(false);
        r202.setDeicingComplete(false);

        Airport a202 = new Airport();
        a202.setFreeGates(8);
        a202.setCapacity(50);
        a202.setTotalRunways(2);
        a202.setAvailableRunways(2);
        a202.setRunwayHeading(180);
        a202.setRunwayLength(3000);
        a202.setLvtoCapability(true);
        a202.setLvtoPermit(true);
        a202.setSpecialPermit(false);

        Crew c202 = new Crew();
        c202.setFlightNumber(202);
        c202.setComplete(true);
        c202.setFdp(7);
        c202.setRestBeforeFlight(14);
        c202.setSectorsToday(2);
        c202.setNightDuty(false);

        flightsRepository.put(202, f202);
        weatherRepository.put(202, w202);
        runwayRepository.put(202, r202);
        airportRepository.put(202, a202);
        crewRepository.put(202, c202);
        alarmsRepository.put(202, new ArrayList<>());

        System.out.println("Flight 202 initialized (expected: DELAY - de-icing)");
    }

    // -------------------------------------------------------
    // Let 303 — DEPART ON TIME
    // Sve nominalno
    // -------------------------------------------------------
    private void initFlight303_DepartOnTime() {
        Aircraft ac303 = new Aircraft();
        ac303.setAge(5);
        ac303.setNextServiceDate(LocalDate.now().plusDays(120));
        ac303.setFlightHoursSinceService(150);
        ac303.setCyclesSinceService(80);
        ac303.setTotalFlightHours(3000);

        Flight f303 = new Flight();
        f303.setFlightNumber(303);
        f303.setRoute("BEG-FRA");
        f303.setAircraft(ac303);
        f303.setCategory(FlightCategory.SCHEDULED_COMMERCIAL);
        f303.setStatus(FlightStatus.SCHEDULED);
        f303.setPlannedDeparture(LocalDateTime.now().plusHours(5));
        f303.setPlannedArrival(LocalDateTime.now().plusHours(7));
        f303.setPassengerCount(160);
        f303.setHasReplacementAircraft(false);
        f303.setHasReplacementCrew(false);

        WeatherReport w303 = new WeatherReport();
        w303.setWindSpeed(15);
        w303.setWindDirection(90);
        w303.setCrosswind(10);
        w303.setTailwind(5);
        w303.setVisibility(10000);
        w303.setTemperature(15);
        w303.setDewPoint(5);
        w303.setPrecipitationType(PrecipitationType.DRY);
        w303.setPrecipitationIntensity(PrecipitationIntensity.NONE);
        w303.setIcingPresent(false);

        Runway r303 = new Runway();
        r303.setStatus(RunwayStatus.OPEN);
        r303.setRwycc(6);
        r303.setRunwaysBeingDeiced(false);
        r303.setDeicingComplete(true);

        Airport a303 = new Airport();
        a303.setFreeGates(10);
        a303.setCapacity(50);
        a303.setTotalRunways(2);
        a303.setAvailableRunways(2);
        a303.setRunwayHeading(90);
        a303.setRunwayLength(3200);
        a303.setLvtoCapability(true);
        a303.setLvtoPermit(true);
        a303.setSpecialPermit(false);

        Crew c303 = new Crew();
        c303.setFlightNumber(303);
        c303.setComplete(true);
        c303.setFdp(6);
        c303.setRestBeforeFlight(16);
        c303.setSectorsToday(1);
        c303.setNightDuty(false);

        flightsRepository.put(303, f303);
        weatherRepository.put(303, w303);
        runwayRepository.put(303, r303);
        airportRepository.put(303, a303);
        crewRepository.put(303, c303);
        alarmsRepository.put(303, new ArrayList<>());

        System.out.println("Flight 303 initialized (expected: DEPART_ON_TIME)");
    }

    private void initFlight404_GeneralAviation() {
        Aircraft ac404 = new Aircraft();
        ac404.setAge(5);
        ac404.setNextServiceDate(LocalDate.now().plusDays(120));
        ac404.setFlightHoursSinceService(100);
        ac404.setCyclesSinceService(50);
        ac404.setTotalFlightHours(2000);

        Flight f404 = new Flight();
        f404.setFlightNumber(404);
        f404.setRoute("BEG-NIS");
        f404.setAircraft(ac404);
        f404.setCategory(FlightCategory.GENERAL_AVIATION);
        f404.setStatus(FlightStatus.SCHEDULED);
        f404.setPlannedDeparture(LocalDateTime.now().plusHours(1));
        f404.setPlannedArrival(LocalDateTime.now().plusHours(2));
        f404.setPassengerCount(4);
        f404.setHasReplacementAircraft(false);
        f404.setHasReplacementCrew(false);

        WeatherReport w404 = new WeatherReport();
        w404.setWindSpeed(15);
        w404.setWindDirection(90);
        w404.setCrosswind(10);
        w404.setTailwind(5);
        w404.setVisibility(5000);
        w404.setTemperature(15);
        w404.setDewPoint(5);
        w404.setPrecipitationType(PrecipitationType.DRY);
        w404.setPrecipitationIntensity(PrecipitationIntensity.NONE);
        w404.setIcingPresent(false);

        Runway r404 = new Runway();
        r404.setStatus(RunwayStatus.OPEN);
        r404.setRwycc(6);
        r404.setRunwaysBeingDeiced(false);
        r404.setDeicingComplete(true);

        Airport a404 = new Airport();
        a404.setFreeGates(5);
        a404.setCapacity(20);
        a404.setTotalRunways(1);
        a404.setAvailableRunways(1);
        a404.setRunwayHeading(90);
        a404.setRunwayLength(600);
        a404.setLvtoCapability(false);
        a404.setLvtoPermit(false);
        a404.setSpecialPermit(false);

        Crew c404 = new Crew();
        c404.setFlightNumber(404);
        c404.setComplete(true);
        c404.setFdp(4);
        c404.setRestBeforeFlight(16);
        c404.setSectorsToday(1);
        c404.setNightDuty(false);

        flightsRepository.put(404, f404);
        weatherRepository.put(404, w404);
        runwayRepository.put(404, r404);
        airportRepository.put(404, a404);
        crewRepository.put(404, c404);
        alarmsRepository.put(404, new ArrayList<>());

        System.out.println("Flight 404 initialized (GENERAL_AVIATION - template test)");
    }

}