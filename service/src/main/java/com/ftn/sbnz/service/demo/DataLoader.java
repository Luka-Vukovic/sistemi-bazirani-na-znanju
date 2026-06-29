package com.ftn.sbnz.service.demo;

import com.ftn.sbnz.model.*;
import com.ftn.sbnz.model.enums.*;
import com.ftn.sbnz.service.entity.*;
import com.ftn.sbnz.service.repository.FlightRepository;
import com.ftn.sbnz.service.util.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Puni H2 bazu pri pokretanju i inicijalizuje in-memory mape za Drools.
 */
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private FlightRepository flightRepository;

    @Override
    public void run(String... args) {
        flightRepository.deleteAll();
        List<FlightEntity> flights = buildFlights();
        flightRepository.saveAll(flights);

        // Punim DroolsConsoleDemo mape iz baze
        for (FlightEntity fe : flights) {
            int fn = fe.getFlightNumber();
            DroolsConsoleDemo.flightsRepository.put(fn, EntityMapper.toModel(fe));
            DroolsConsoleDemo.weatherRepository.put(fn, EntityMapper.toModel(fe.getWeather()));
            DroolsConsoleDemo.runwayRepository.put(fn, EntityMapper.toModel(fe.getRunway()));
            DroolsConsoleDemo.airportRepository.put(fn, EntityMapper.toModel(fe.getAirport()));
            DroolsConsoleDemo.crewRepository.put(fn, EntityMapper.toModel(fe.getCrew()));
            DroolsConsoleDemo.alarmsRepository.put(fn, EntityMapper.alarmsToModel(fe.getAlarms()));
        }

        System.out.println("=== DataLoader: " + flights.size() + " flights loaded into H2 and Drools cache ===");
    }

    private List<FlightEntity> buildFlights() {
        List<FlightEntity> list = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        // ── 101: CANCEL — nulta vidljivost, kritična pista, posada prekoračila normu
        list.add(flight(101, "BEG-LHR", FlightCategory.SCHEDULED_COMMERCIAL, FlightStatus.DELAYED, 90, 180, false, false,
            now.plusHours(2), now.plusHours(4),
            aircraft(10, 60, 200, 100, 8000),
            weather(80, 270, 70, 25, 40, -5, -5, PrecipitationType.RAIN, PrecipitationIntensity.MODERATE, false),
            runway(RunwayStatus.OPEN, 1, false, false),
            airport(5, 50, 2, 2, 270, 3200, false, false, false),
            crew(101, true, false, 14, 10, 3),
            List.of()));

        // ── 202: DELAY de-icing — mraz, rosište = temperatura
        list.add(flight(202, "BEG-CDG", FlightCategory.SCHEDULED_COMMERCIAL, FlightStatus.DELAYED, 150, 150, false, false,
            now.plusHours(4), now.plusHours(6),
            aircraft(8, 90, 300, 150, 5000),
            weather(15, 180, 10, 5, 3000, -5, -5, PrecipitationType.DRY, PrecipitationIntensity.NONE, false),
            runway(RunwayStatus.OPEN, 4, false, false),
            airport(8, 50, 2, 2, 180, 3000, true, true, false),
            crew(202, true, false, 7, 14, 2),
            List.of()));

        // ── 303: DEPART ON TIME — sve nominalno
        list.add(flight(303, "BEG-FRA", FlightCategory.SCHEDULED_COMMERCIAL, FlightStatus.ON_TIME, 0, 160, false, false,
            now.plusHours(5), now.plusHours(7),
            aircraft(5, 120, 150, 80, 3000),
            weather(15, 90, 10, 5, 10000, 15, 5, PrecipitationType.DRY, PrecipitationIntensity.NONE, false),
            runway(RunwayStatus.OPEN, 6, false, true),
            airport(10, 50, 2, 2, 90, 3200, true, true, false),
            crew(303, true, false, 6, 16, 1),
            List.of()));

        // ── 404: CANCEL — GENERAL_AVIATION, pista prekratka (600m < 800m)
        list.add(flight(404, "BEG-NIS", FlightCategory.GENERAL_AVIATION, FlightStatus.CANCELLED, 0, 4, false, false,
            now.plusHours(1), now.plusHours(2),
            aircraft(5, 120, 100, 50, 2000),
            weather(15, 90, 10, 5, 5000, 15, 5, PrecipitationType.DRY, PrecipitationIntensity.NONE, false),
            runway(RunwayStatus.OPEN, 6, false, true),
            airport(5, 20, 1, 1, 90, 600, false, false, false),
            crew(404, true, false, 4, 16, 1),
            List.of()));

        // ── 505: DELAY — runway MAINTENANCE, zamena aviona dostupna
        list.add(flight(505, "BEG-SKP", FlightCategory.SCHEDULED_COMMERCIAL, FlightStatus.DELAYED, 45, 120, true, false,
            now.plusHours(1), now.plusHours(2),
            aircraft(5, 120, 100, 50, 2000),
            weather(20, 90, 15, 5, 8000, 18, 5, PrecipitationType.DRY, PrecipitationIntensity.NONE, false),
            runway(RunwayStatus.MAINTENANCE, 6, false, true),
            airport(5, 50, 2, 2, 90, 3000, true, true, false),
            crew(505, true, false, 6, 14, 1),
            List.of()));

        // ── 606: CANCEL — aktivan led + jak bocni vetar + kriticna vidljivost
        list.add(flight(606, "BEG-IST", FlightCategory.SCHEDULED_COMMERCIAL, FlightStatus.DELAYED, 120, 210, false, false,
            now.plusHours(3), now.plusHours(6),
            aircraft(12, 30, 450, 220, 12000),
            weather(70, 180, 60, 10, 350, -3, -3, PrecipitationType.RAIN, PrecipitationIntensity.HEAVY, true),
            runway(RunwayStatus.OPEN, 3, true, false),
            airport(3, 50, 2, 2, 180, 3000, true, false, false),
            crew(606, true, false, 9, 13, 2),
            List.of()));

        // ── 707: CANCEL — motori ne mogu da se pokrenu, temp < -40°C
        list.add(flight(707, "BEG-OSL", FlightCategory.SCHEDULED_COMMERCIAL, FlightStatus.CANCELLED, 0, 195, false, false,
            now.plusHours(6), now.plusHours(9),
            aircraft(7, 45, 180, 90, 4500),
            weather(30, 360, 20, 8, 2000, -45, -45, PrecipitationType.SNOW, PrecipitationIntensity.HEAVY, true),
            runway(RunwayStatus.OPEN, 2, true, false),
            airport(6, 50, 2, 2, 360, 3500, true, true, false),
            crew(707, true, false, 8, 12, 2),
            List.of()));

        // ── 808: REDIRECT — aerodrom pod pritiskom + loši vremenski uslovi
        list.add(flight(808, "BEG-VIE", FlightCategory.BUSINESS_AVIATION, FlightStatus.DELAYED, 60, 8, false, false,
            now.plusHours(2), now.plusHours(3),
            aircraft(3, 200, 80, 40, 1500),
            weather(55, 270, 50, 20, 380, -2, -2, PrecipitationType.RAIN, PrecipitationIntensity.MODERATE, false),
            runway(RunwayStatus.OPEN, 5, false, true),
            airport(1, 20, 4, 2, 270, 2800, true, true, false),
            crew(808, true, false, 7, 15, 2),
            List.of()));

        // ── 909: DELAY — posada umorna + zamena posade dostupna
        list.add(flight(909, "BEG-MAD", FlightCategory.SCHEDULED_COMMERCIAL, FlightStatus.DELAYED, 75, 185, false, true,
            now.plusHours(3), now.plusHours(7),
            aircraft(9, 60, 350, 180, 9000),
            weather(25, 225, 18, 6, 6000, 8, 2, PrecipitationType.DRY, PrecipitationIntensity.NONE, false),
            runway(RunwayStatus.OPEN, 6, false, true),
            airport(7, 50, 2, 2, 225, 3200, true, true, false),
            crew(909, true, false, 11, 9, 3),
            List.of()));

        // ── 1010: CANCEL — MEL kategorija A alarm
        list.add(flight(1010, "BEG-ATH", FlightCategory.SCHEDULED_COMMERCIAL, FlightStatus.CANCELLED, 0, 168, false, false,
            now.plusHours(4), now.plusHours(7),
            aircraft(11, 20, 520, 260, 15000),
            weather(20, 180, 15, 5, 7000, 12, 4, PrecipitationType.DRY, PrecipitationIntensity.NONE, false),
            runway(RunwayStatus.OPEN, 6, false, true),
            airport(8, 50, 2, 2, 180, 3000, true, true, false),
            crew(1010, true, false, 8, 14, 2),
            List.of(
                alarm(1010, AlarmType.ENGINE, "Left Engine FADEC", AlarmSeverity.CRITICAL, AlarmStatus.ACTIVE, MelCategory.A)
            )));

        // ── 1111: DELAY — jedina CARGO ruta, visok operativni rizik
        list.add(flight(1111, "BEG-DXB", FlightCategory.CARGO, FlightStatus.DELAYED, 90, 0, false, false,
            now.plusHours(7), now.plusHours(14),
            aircraft(18, 15, 490, 240, 28000),
            weather(40, 270, 35, 12, 1200, -1, -2, PrecipitationType.FOG, PrecipitationIntensity.LIGHT, false),
            runway(RunwayStatus.OPEN, 4, false, false),
            airport(4, 50, 2, 2, 270, 3800, true, true, false),
            crew(1111, true, false, 9, 11, 3),
            List.of(
                alarm(1111, AlarmType.AVIONICS, "Navigation FMC", AlarmSeverity.HIGH, AlarmStatus.ACTIVE, MelCategory.B),
                alarm(1111, AlarmType.ELECTRICAL, "APU Electrical Bus", AlarmSeverity.MEDIUM, AlarmStatus.ACTIVE, MelCategory.C)
            )));

        // ── 1212: DEPART ON TIME — BUSINESS_AVIATION, sve nominalno
        list.add(flight(1212, "BEG-ZRH", FlightCategory.BUSINESS_AVIATION, FlightStatus.ON_TIME, 0, 6, true, true,
            now.plusHours(1), now.plusHours(2),
            aircraft(4, 300, 120, 60, 2000),
            weather(12, 90, 8, 3, 9000, 20, 10, PrecipitationType.DRY, PrecipitationIntensity.NONE, false),
            runway(RunwayStatus.OPEN, 6, false, true),
            airport(12, 30, 2, 2, 90, 2500, true, true, true),
            crew(1212, true, false, 5, 18, 1),
            List.of()));

        // ── 1313: CANCEL — ledena kiša, nema holdover time
        list.add(flight(1313, "BEG-PRG", FlightCategory.SCHEDULED_COMMERCIAL, FlightStatus.CANCELLED, 0, 142, false, false,
            now.plusHours(2), now.plusHours(4),
            aircraft(6, 90, 200, 100, 6000),
            weather(25, 270, 20, 8, 4000, -1, -2, PrecipitationType.FREEZING_RAIN, PrecipitationIntensity.MODERATE, true),
            runway(RunwayStatus.OPEN, 3, true, false),
            airport(6, 50, 2, 2, 270, 3000, true, true, false),
            crew(1313, true, false, 7, 15, 2),
            List.of()));

        // ── 1414: DELAY — servis prekoračen, stari avion, zamena dostupna
        list.add(flight(1414, "BEG-SOF", FlightCategory.SCHEDULED_COMMERCIAL, FlightStatus.DELAYED, 30, 155, true, false,
            now.plusHours(3), now.plusHours(4),
            aircraft(26, 10, 610, 310, 45000),
            weather(18, 180, 12, 4, 8000, 10, 3, PrecipitationType.DRY, PrecipitationIntensity.NONE, false),
            runway(RunwayStatus.OPEN, 6, false, true),
            airport(7, 50, 2, 2, 180, 3000, true, true, false),
            crew(1414, true, false, 6, 15, 2),
            List.of(
                alarm(1414, AlarmType.STRUCTURAL, "Fuselage Section 41", AlarmSeverity.MEDIUM, AlarmStatus.ACTIVE, MelCategory.C)
            )));

        // ── 1515: CANCEL — posada nekompletna, nema zamene, pista zatvorena
        list.add(flight(1515, "BEG-BUD", FlightCategory.SCHEDULED_COMMERCIAL, FlightStatus.CANCELLED, 0, 138, false, false,
            now.plusHours(1), now.plusHours(2),
            aircraft(7, 80, 220, 110, 7500),
            weather(22, 90, 16, 6, 6000, 14, 5, PrecipitationType.DRY, PrecipitationIntensity.NONE, false),
            runway(RunwayStatus.CLOSED, 6, false, false),
            airport(5, 50, 2, 1, 90, 3000, true, true, false),
            crew(1515, false, false, 6, 14, 2),
            List.of()));

        return list;
    }

    // ── Builder helpers ──

    private FlightEntity flight(int fn, String route, FlightCategory cat, FlightStatus status,
                                 int delay, int pax, boolean replAc, boolean replCrew,
                                 LocalDateTime dep, LocalDateTime arr,
                                 AircraftEntity ac, WeatherReportEntity w,
                                 RunwayEntity rw, AirportEntity ap, CrewEntity crew,
                                 List<TechnicalAlarmEntity> alarms) {
        FlightEntity f = new FlightEntity();
        f.setFlightNumber(fn);
        f.setRoute(route);
        f.setCategory(cat);
        f.setStatus(status);
        f.setDelayMinutes(delay);
        f.setPassengerCount(pax);
        f.setHasReplacementAircraft(replAc);
        f.setHasReplacementCrew(replCrew);
        f.setPlannedDeparture(dep);
        f.setPlannedArrival(arr);
        f.setAircraft(ac);
        f.setWeather(w);
        f.setRunway(rw);
        f.setAirport(ap);
        f.setCrew(crew);
        f.setAlarms(new ArrayList<>(alarms));
        return f;
    }

    private AircraftEntity aircraft(int age, int daysToService, int hoursSinceService,
                                     int cyclesSinceService, int totalHours) {
        AircraftEntity e = new AircraftEntity();
        e.setAge(age);
        e.setNextServiceDate(LocalDate.now().plusDays(daysToService));
        e.setFlightHoursSinceService(hoursSinceService);
        e.setCyclesSinceService(cyclesSinceService);
        e.setTotalFlightHours(totalHours);
        return e;
    }

    private WeatherReportEntity weather(int windSpeed, int windDir, int crosswind, int tailwind,
                                         int visibility, int temp, int dewPoint,
                                         PrecipitationType precType, PrecipitationIntensity precInt,
                                         boolean icing) {
        WeatherReportEntity e = new WeatherReportEntity();
        e.setWindSpeed(windSpeed);
        e.setWindDirection(windDir);
        e.setCrosswind(crosswind);
        e.setTailwind(tailwind);
        e.setVisibility(visibility);
        e.setTemperature(temp);
        e.setDewPoint(dewPoint);
        e.setPrecipitationType(precType);
        e.setPrecipitationIntensity(precInt);
        e.setIcingPresent(icing);
        return e;
    }

    private RunwayEntity runway(RunwayStatus status, int rwycc,
                                 boolean beingDeiced, boolean deicingComplete) {
        RunwayEntity e = new RunwayEntity();
        e.setStatus(status);
        e.setRwycc(rwycc);
        e.setRunwaysBeingDeiced(beingDeiced);
        e.setDeicingComplete(deicingComplete);
        return e;
    }

    private AirportEntity airport(int freeGates, int capacity, int totalRunways,
                                   int availableRunways, int heading, int length,
                                   boolean lvtoCap, boolean lvtoPermit, boolean specialPermit) {
        AirportEntity e = new AirportEntity();
        e.setFreeGates(freeGates);
        e.setCapacity(capacity);
        e.setTotalRunways(totalRunways);
        e.setAvailableRunways(availableRunways);
        e.setRunwayHeading(heading);
        e.setRunwayLength(length);
        e.setLvtoCapability(lvtoCap);
        e.setLvtoPermit(lvtoPermit);
        e.setSpecialPermit(specialPermit);
        return e;
    }

    private CrewEntity crew(int flightNumber, boolean complete, boolean nightDuty,
                             double fdp, double rest, int sectors) {
        CrewEntity e = new CrewEntity();
        e.setFlightNumber(flightNumber);
        e.setComplete(complete);
        e.setNightDuty(nightDuty);
        e.setFdp(fdp);
        e.setRestBeforeFlight(rest);
        e.setSectorsToday(sectors);
        return e;
    }

    private TechnicalAlarmEntity alarm(int flightNumber, AlarmType type, String component,
                                        AlarmSeverity severity, AlarmStatus status, MelCategory mel) {
        TechnicalAlarmEntity e = new TechnicalAlarmEntity();
        e.setFlightNumber(flightNumber);
        e.setAlarmType(type);
        e.setComponent(component);
        e.setSeverity(severity);
        e.setReportedAt(LocalDateTime.now().minusMinutes(30));
        e.setStatus(status);
        e.setMelCategory(mel);
        return e;
    }
}
