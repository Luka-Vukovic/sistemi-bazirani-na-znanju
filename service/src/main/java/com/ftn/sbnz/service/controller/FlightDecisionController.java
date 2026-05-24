package com.ftn.sbnz.service.controller;

import com.ftn.sbnz.model.*;
import com.ftn.sbnz.model.dto.FlightReportDTO;
import com.ftn.sbnz.service.service.FlightDecisionService;
import com.ftn.sbnz.service.demo.DroolsConsoleDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/flights")
public class FlightDecisionController {

    @Autowired
    private FlightDecisionService flightDecisionService;

    @GetMapping("/{flightNumber}/recommendation")
    public ResponseEntity<?> getFlightRecommendation(@PathVariable int flightNumber) {
        
        Flight flight = DroolsConsoleDemo.flightsRepository.get(flightNumber);
        WeatherReport weather = DroolsConsoleDemo.weatherRepository.get(flightNumber);
        Runway runway = DroolsConsoleDemo.runwayRepository.get(flightNumber);

        if (flight == null || weather == null || runway == null) {
            return ResponseEntity.status(404).body("Flight " + flightNumber + " not found in the repository.");
        }

        FlightReportDTO report = flightDecisionService.evaluateFlight(flight, weather, runway);
        
        return ResponseEntity.ok(report);
    }
}