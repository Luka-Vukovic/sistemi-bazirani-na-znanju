package com.ftn.sbnz.service.controller;

import com.ftn.sbnz.model.Flight;
import com.ftn.sbnz.model.enums.AlarmSeverity;
import com.ftn.sbnz.service.demo.DroolsConsoleDemo;
import com.ftn.sbnz.service.service.CepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cep")
public class CepController {

    @Autowired
    private CepService cepService;

    @PostMapping("/{flightNumber}/weather")
    public ResponseEntity<?> sendWeatherEvent(
            @PathVariable int flightNumber,
            @RequestParam int windSpeed,
            @RequestParam int visibility,
            @RequestParam int temperature) {

        Flight flight = DroolsConsoleDemo.flightsRepository.get(flightNumber);
        if (flight == null) return ResponseEntity.status(404)
                .body("Flight " + flightNumber + " not found.");

        cepService.sendWeatherEvent(flightNumber, flight, windSpeed, visibility, temperature);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("flightNumber", flightNumber);
        response.put("weatherDeteriorationAlert", cepService.hasWeatherDeteriorationAlert(flightNumber));
        response.put("technicalIncident", cepService.hasTechnicalIncident(flightNumber));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{flightNumber}/alarm")
    public ResponseEntity<?> sendAlarmEvent(
            @PathVariable int flightNumber,
            @RequestParam AlarmSeverity severity) {

        Flight flight = DroolsConsoleDemo.flightsRepository.get(flightNumber);
        if (flight == null) return ResponseEntity.status(404)
                .body("Flight " + flightNumber + " not found.");

        cepService.sendAlarmEvent(flightNumber, flight, severity);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("flightNumber", flightNumber);
        response.put("weatherDeteriorationAlert", cepService.hasWeatherDeteriorationAlert(flightNumber));
        response.put("technicalIncident", cepService.hasTechnicalIncident(flightNumber));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{flightNumber}/status")
    public ResponseEntity<?> getCepStatus(@PathVariable int flightNumber) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("flightNumber", flightNumber);
        response.put("weatherDeteriorationAlert", cepService.hasWeatherDeteriorationAlert(flightNumber));
        response.put("technicalIncident", cepService.hasTechnicalIncident(flightNumber));
        return ResponseEntity.ok(response);
    }
}