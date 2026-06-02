package com.ftn.sbnz.service.controller;

import com.ftn.sbnz.model.*;
import com.ftn.sbnz.model.dto.FlightReportDTO;
import com.ftn.sbnz.service.service.FlightDecisionService;
import com.ftn.sbnz.service.demo.DroolsConsoleDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightDecisionController {

    @Autowired
    private FlightDecisionService flightDecisionService;

    @GetMapping("/{flightNumber}/recommendation")
    public ResponseEntity<?> getRecommendation(@PathVariable int flightNumber) {
        Object[] data = resolve(flightNumber);
        if (data == null) return notFound(flightNumber);

        FlightReportDTO report = flightDecisionService.evaluateFlight(
                (Flight) data[0], (WeatherReport) data[1], (Runway) data[2],
                (Airport) data[3], (Crew) data[4], (List<TechnicalAlarm>) data[5]);

        return ResponseEntity.ok(report);
    }

    @GetMapping("/{flightNumber}/conditions")
    public ResponseEntity<?> getConditions(@PathVariable int flightNumber) {
        Object[] data = resolve(flightNumber);
        if (data == null) return notFound(flightNumber);

        FlightReportDTO report = flightDecisionService.evaluateFlightWithConditions(
                (Flight) data[0], (WeatherReport) data[1], (Runway) data[2],
                (Airport) data[3], (Crew) data[4], (List<TechnicalAlarm>) data[5]);

        return ResponseEntity.ok(report);
    }

    private Object[] resolve(int flightNumber) {
        Flight flight         = DroolsConsoleDemo.flightsRepository.get(flightNumber);
        WeatherReport weather = DroolsConsoleDemo.weatherRepository.get(flightNumber);
        Runway runway         = DroolsConsoleDemo.runwayRepository.get(flightNumber);
        Airport airport       = DroolsConsoleDemo.airportRepository.get(flightNumber);
        Crew crew             = DroolsConsoleDemo.crewRepository.get(flightNumber);
        List<TechnicalAlarm> alarms = DroolsConsoleDemo.alarmsRepository.get(flightNumber);

        if (flight == null || weather == null || runway == null
                || airport == null || crew == null || alarms == null) return null;

        return new Object[]{flight, weather, runway, airport, crew, alarms};
    }

    private ResponseEntity<String> notFound(int flightNumber) {
        return ResponseEntity.status(404)
                .body("Flight " + flightNumber + " not found.");
    }
}