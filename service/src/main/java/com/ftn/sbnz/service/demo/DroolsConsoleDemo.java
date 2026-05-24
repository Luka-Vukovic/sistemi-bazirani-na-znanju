package com.ftn.sbnz.service.demo;

import com.ftn.sbnz.model.*;
import com.ftn.sbnz.model.enums.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class DroolsConsoleDemo implements CommandLineRunner {

    public static final Map<Integer, Flight> flightsRepository = new HashMap<>();
    public static final Map<Integer, WeatherReport> weatherRepository = new HashMap<>();
    public static final Map<Integer, Runway> runwayRepository = new HashMap<>();

    @Override
    public void run(String... args) throws Exception {
        System.out.println("==================================================");
        System.out.println("   INITIALIZING EXISTENT FLIGHTS IN REPOSITORY    ");
        System.out.println("==================================================\n");

        Flight f101 = new Flight();
        f101.setFlightNumber(101);
        f101.setStatus(FlightStatus.SCHEDULED);
        f101.setPlannedDeparture(LocalDateTime.now().plusHours(2));
        f101.setHasReplacementAircraft(false);

        WeatherReport w101 = new WeatherReport();
        w101.setTailwind(25);
        w101.setVisibility(50);

        Runway r101 = new Runway();
        r101.setStatus(RunwayStatus.OPEN);
        r101.setRwycc(1);
        r101.setRunwaysBeingDeiced(false);
        r101.setDeicingComplete(false);

        flightsRepository.put(101, f101);
        weatherRepository.put(101, w101);
        runwayRepository.put(101, r101);

        Flight f202 = new Flight();
        f202.setFlightNumber(202);
        f202.setStatus(FlightStatus.SCHEDULED);
        f202.setPlannedDeparture(LocalDateTime.now().plusHours(4));
        f202.setHasReplacementAircraft(false);

        WeatherReport w202 = new WeatherReport();
        w202.setTemperature(-5);
        w202.setDewPoint(-5);

        Runway r202 = new Runway();
        r202.setStatus(RunwayStatus.OPEN);
        r202.setRwycc(4);
        r202.setRunwaysBeingDeiced(false);
        r202.setDeicingComplete(false);

        flightsRepository.put(202, f202);
        weatherRepository.put(202, w202);
        runwayRepository.put(202, r202);

        Flight f303 = new Flight();
        f303.setFlightNumber(303);
        f303.setStatus(FlightStatus.SCHEDULED);
        f303.setPlannedDeparture(LocalDateTime.now().plusHours(5));
        f303.setHasReplacementAircraft(false);

        WeatherReport w303 = new WeatherReport();
        w303.setTailwind(5);
        w303.setVisibility(10000);
        w303.setTemperature(15);
        w303.setDewPoint(5);

        Runway r303 = new Runway();
        r303.setStatus(RunwayStatus.OPEN);
        r303.setRwycc(6);
        r303.setRunwaysBeingDeiced(false);
        r303.setDeicingComplete(true);

        flightsRepository.put(303, f303);
        weatherRepository.put(303, w303);
        runwayRepository.put(303, r303);

        System.out.println("==================================================\n");
    }
}