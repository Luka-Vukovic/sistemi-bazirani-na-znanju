package com.ftn.sbnz.service.util;

import com.ftn.sbnz.model.*;
import com.ftn.sbnz.service.entity.*;

import java.util.ArrayList;
import java.util.List;

public class EntityMapper {

    public static Aircraft toModel(AircraftEntity e) {
        if (e == null) return new Aircraft();
        Aircraft a = new Aircraft();
        a.setAge(e.getAge());
        a.setNextServiceDate(e.getNextServiceDate());
        a.setFlightHoursSinceService(e.getFlightHoursSinceService());
        a.setCyclesSinceService(e.getCyclesSinceService());
        a.setTotalFlightHours(e.getTotalFlightHours());
        return a;
    }

    public static WeatherReport toModel(WeatherReportEntity e) {
        if (e == null) return new WeatherReport();
        WeatherReport w = new WeatherReport();
        w.setWindSpeed(e.getWindSpeed());
        w.setWindDirection(e.getWindDirection());
        w.setCrosswind(e.getCrosswind());
        w.setTailwind(e.getTailwind());
        w.setVisibility(e.getVisibility());
        w.setTemperature(e.getTemperature());
        w.setDewPoint(e.getDewPoint());
        w.setPrecipitationType(e.getPrecipitationType());
        w.setPrecipitationIntensity(e.getPrecipitationIntensity());
        w.setIcingPresent(e.isIcingPresent());
        return w;
    }

    public static Runway toModel(RunwayEntity e) {
        if (e == null) return new Runway();
        Runway r = new Runway();
        r.setStatus(e.getStatus());
        r.setRwycc(e.getRwycc());
        r.setRunwaysBeingDeiced(e.isRunwaysBeingDeiced());
        r.setDeicingComplete(e.isDeicingComplete());
        return r;
    }

    public static Airport toModel(AirportEntity e) {
        if (e == null) return new Airport();
        Airport a = new Airport();
        a.setFreeGates(e.getFreeGates());
        a.setCapacity(e.getCapacity());
        a.setTotalRunways(e.getTotalRunways());
        a.setAvailableRunways(e.getAvailableRunways());
        a.setRunwayHeading(e.getRunwayHeading());
        a.setRunwayLength(e.getRunwayLength());
        a.setLvtoCapability(e.isLvtoCapability());
        a.setLvtoPermit(e.isLvtoPermit());
        a.setSpecialPermit(e.isSpecialPermit());
        return a;
    }

    public static Crew toModel(CrewEntity e) {
        if (e == null) return new Crew();
        Crew c = new Crew();
        c.setFlightNumber(e.getFlightNumber());
        c.setComplete(e.isComplete());
        c.setFdp((int) e.getFdp());
        c.setRestBeforeFlight((int) e.getRestBeforeFlight());
        c.setSectorsToday(e.getSectorsToday());
        c.setNightDuty(e.isNightDuty());
        return c;
    }

    public static TechnicalAlarm toModel(TechnicalAlarmEntity e) {
        if (e == null) return null;
        TechnicalAlarm a = new TechnicalAlarm();
        a.setFlightNumber(e.getFlightNumber());
        a.setAlarmType(e.getAlarmType());
        a.setComponent(e.getComponent());
        a.setSeverity(e.getSeverity());
        a.setReportedAt(e.getReportedAt());
        a.setStatus(e.getStatus());
        a.setMelCategory(e.getMelCategory());
        return a;
    }

    public static Flight toModel(FlightEntity e) {
        if (e == null) return null;
        Flight f = new Flight();
        f.setFlightNumber(e.getFlightNumber());
        f.setRoute(e.getRoute());
        f.setCategory(e.getCategory());
        f.setStatus(e.getStatus());
        f.setPlannedDeparture(e.getPlannedDeparture());
        f.setPlannedArrival(e.getPlannedArrival());
        f.setPassengerCount(e.getPassengerCount());
        f.setDelayMinutes(e.getDelayMinutes());
        f.setHasReplacementAircraft(e.isHasReplacementAircraft());
        f.setHasReplacementCrew(e.isHasReplacementCrew());
        f.setAircraft(toModel(e.getAircraft()));
        return f;
    }

    public static List<TechnicalAlarm> alarmsToModel(List<TechnicalAlarmEntity> entities) {
        List<TechnicalAlarm> result = new ArrayList<>();
        if (entities != null) entities.forEach(e -> result.add(toModel(e)));
        return result;
    }

    // ── Entity builders ──

    public static AircraftEntity toEntity(Aircraft a) {
        AircraftEntity e = new AircraftEntity();
        e.setAge(a.getAge());
        e.setNextServiceDate(a.getNextServiceDate());
        e.setFlightHoursSinceService(a.getFlightHoursSinceService());
        e.setCyclesSinceService(a.getCyclesSinceService());
        e.setTotalFlightHours(a.getTotalFlightHours());
        return e;
    }

    public static WeatherReportEntity toEntity(WeatherReport w) {
        WeatherReportEntity e = new WeatherReportEntity();
        e.setWindSpeed(w.getWindSpeed());
        e.setWindDirection(w.getWindDirection());
        e.setCrosswind(w.getCrosswind());
        e.setTailwind(w.getTailwind());
        e.setVisibility(w.getVisibility());
        e.setTemperature(w.getTemperature());
        e.setDewPoint(w.getDewPoint());
        e.setPrecipitationType(w.getPrecipitationType());
        e.setPrecipitationIntensity(w.getPrecipitationIntensity());
        e.setIcingPresent(w.isIcingPresent());
        return e;
    }

    public static RunwayEntity toEntity(Runway r) {
        RunwayEntity e = new RunwayEntity();
        e.setStatus(r.getStatus());
        e.setRwycc(r.getRwycc());
        e.setRunwaysBeingDeiced(r.isRunwaysBeingDeiced());
        e.setDeicingComplete(r.isDeicingComplete());
        return e;
    }

    public static AirportEntity toEntity(Airport a) {
        AirportEntity e = new AirportEntity();
        e.setFreeGates(a.getFreeGates());
        e.setCapacity(a.getCapacity());
        e.setTotalRunways(a.getTotalRunways());
        e.setAvailableRunways(a.getAvailableRunways());
        e.setRunwayHeading(a.getRunwayHeading());
        e.setRunwayLength(a.getRunwayLength());
        e.setLvtoCapability(a.isLvtoCapability());
        e.setLvtoPermit(a.isLvtoPermit());
        e.setSpecialPermit(a.isSpecialPermit());
        return e;
    }

    public static CrewEntity toEntity(Crew c) {
        CrewEntity e = new CrewEntity();
        e.setFlightNumber(c.getFlightNumber());
        e.setComplete(c.getIsComplete());
        e.setFdp(c.getFdp());
        e.setRestBeforeFlight(c.getRestBeforeFlight());
        e.setSectorsToday(c.getSectorsToday());
        e.setNightDuty(c.getIsNightDuty());
        return e;
    }
}
