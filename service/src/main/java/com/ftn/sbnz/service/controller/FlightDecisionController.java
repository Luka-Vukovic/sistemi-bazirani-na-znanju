package com.ftn.sbnz.service.controller;

import com.ftn.sbnz.model.*;
import com.ftn.sbnz.model.enums.*;
import com.ftn.sbnz.model.dto.AccumulateReportDTO;
import com.ftn.sbnz.model.dto.FlightReportDTO;
import com.ftn.sbnz.service.entity.FlightEntity;
import com.ftn.sbnz.service.entity.AircraftEntity;
import com.ftn.sbnz.service.entity.AirportEntity;
import com.ftn.sbnz.service.entity.CrewEntity;
import com.ftn.sbnz.service.entity.RunwayEntity;
import com.ftn.sbnz.service.entity.TechnicalAlarmEntity;
import com.ftn.sbnz.service.entity.WeatherReportEntity;
import com.ftn.sbnz.service.service.CepService;
import com.ftn.sbnz.service.service.FlightDecisionService;
import com.ftn.sbnz.service.service.FlightStorageService;
import com.ftn.sbnz.service.util.EntityMapper;
import com.ftn.sbnz.service.demo.DroolsConsoleDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/flights")
@CrossOrigin(origins = "http://localhost:4200")
public class FlightDecisionController {

    @Autowired private FlightDecisionService flightDecisionService;
    @Autowired private CepService cepService;
    @Autowired private FlightStorageService storageService;

    // ==================== LIST ====================

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllFlights(
            @RequestParam(required = false) String date) {

        List<FlightEntity> entities = (date != null)
            ? storageService.findByDate(LocalDate.parse(date))
            : storageService.findAll();

        List<Map<String, Object>> result = new ArrayList<>();
        for (FlightEntity fe : entities) {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("flightNumber", fe.getFlightNumber());
            entry.put("route", fe.getRoute());
            entry.put("category", fe.getCategory());
            entry.put("status", fe.getStatus());
            entry.put("plannedDeparture", fe.getPlannedDeparture());
            entry.put("plannedArrival", fe.getPlannedArrival());
            entry.put("passengerCount", fe.getPassengerCount());
            entry.put("delayMinutes", fe.getDelayMinutes());
            entry.put("hasReplacementAircraft", fe.isHasReplacementAircraft());
            entry.put("hasReplacementCrew", fe.isHasReplacementCrew());
            if (fe.getWeather() != null) {
                entry.put("visibility", fe.getWeather().getVisibility());
                entry.put("windSpeed", fe.getWeather().getWindSpeed());
                entry.put("temperature", fe.getWeather().getTemperature());
            }
            if (fe.getRunway() != null) {
                entry.put("runwayStatus", fe.getRunway().getStatus());
                entry.put("rwycc", fe.getRunway().getRwycc());
            }
            result.add(entry);
        }

        result.sort((a, b) -> Integer.compare((Integer) a.get("flightNumber"), (Integer) b.get("flightNumber")));
        return ResponseEntity.ok(result);
    }

    // ==================== DETAIL ====================

    @GetMapping("/{flightNumber}/detail")
    public ResponseEntity<?> getFlightDetail(@PathVariable int flightNumber) {
        Optional<FlightEntity> opt = storageService.findById(flightNumber);
        if (opt.isEmpty()) return notFound(flightNumber);
        FlightEntity fe = opt.get();

        Map<String, Object> detail = new LinkedHashMap<>();
        detail.put("flight", EntityMapper.toModel(fe));
        detail.put("weather", EntityMapper.toModel(fe.getWeather()));
        detail.put("runway", EntityMapper.toModel(fe.getRunway()));
        detail.put("airport", EntityMapper.toModel(fe.getAirport()));
        detail.put("crew", EntityMapper.toModel(fe.getCrew()));
        detail.put("alarms", EntityMapper.alarmsToModel(fe.getAlarms()));
        detail.put("alarmCounts", cepService.getAlarmCounts(flightNumber));
        return ResponseEntity.ok(detail);
    }

    // ==================== RECOMMENDATION ====================

    @GetMapping("/{flightNumber}/recommendation")
    public ResponseEntity<?> getRecommendation(@PathVariable int flightNumber) {
        Object[] data = resolve(flightNumber);
        if (data == null) return notFound(flightNumber);

        FlightReportDTO report = flightDecisionService.evaluateFlight(
            (Flight) data[0], (WeatherReport) data[1], (Runway) data[2],
            (Airport) data[3], (Crew) data[4], (List<TechnicalAlarm>) data[5]);

        updateStatus(flightNumber, report);
        return ResponseEntity.ok(report);
    }

    // ==================== CONDITIONS ====================

    @GetMapping("/{flightNumber}/conditions")
    public ResponseEntity<?> getConditions(@PathVariable int flightNumber) {
        Object[] data = resolve(flightNumber);
        if (data == null) return notFound(flightNumber);

        FlightReportDTO report = flightDecisionService.evaluateFlightWithConditions(
            (Flight) data[0], (WeatherReport) data[1], (Runway) data[2],
            (Airport) data[3], (Crew) data[4], (List<TechnicalAlarm>) data[5]);

        updateStatus(flightNumber, report);
        return ResponseEntity.ok(report);
    }

    // ==================== ACCUMULATE ====================

    @GetMapping("/accumulate-report")
    public ResponseEntity<AccumulateReportDTO> getAccumulateReport() {
        List<FlightData> flightDataList = new ArrayList<>();
        for (FlightEntity fe : storageService.findAll()) {
            flightDataList.add(new FlightData(
                EntityMapper.toModel(fe),
                EntityMapper.toModel(fe.getWeather())
            ));
        }
        return ResponseEntity.ok(flightDecisionService.generateAccumulateReport(flightDataList));
    }

    // ==================== CEP STATUS ====================

    @GetMapping("/{flightNumber}/cep-status")
    public ResponseEntity<?> getCepStatus(@PathVariable int flightNumber) {
        if (!storageService.exists(flightNumber)) return notFound(flightNumber);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("flightNumber", flightNumber);
        response.put("weatherDeteriorationAlert", cepService.hasWeatherDeteriorationAlert(flightNumber));
        response.put("technicalIncident", cepService.hasTechnicalIncident(flightNumber));
        response.put("alarmCounts", cepService.getAlarmCounts(flightNumber));
        return ResponseEntity.ok(response);
    }

    // ==================== CRUD ====================

    @PostMapping
    public ResponseEntity<?> createFlight(@RequestBody Map<String, Object> body) {
        try {
            int fn = toInt(body.get("flightNumber"));
            if (storageService.exists(fn))
                return ResponseEntity.badRequest().body("Flight " + fn + " already exists.");

            FlightEntity entity = buildEntity(fn, body);
            storageService.save(entity);
            syncToCache(entity);
            return ResponseEntity.ok(Map.of("flightNumber", fn, "message", "Flight created."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid data: " + e.getMessage());
        }
    }

    @PutMapping("/{flightNumber}")
    public ResponseEntity<?> updateFlight(@PathVariable int flightNumber,
                                           @RequestBody Map<String, Object> body) {
        if (!storageService.exists(flightNumber)) return notFound(flightNumber);
        try {
            FlightEntity entity = buildEntity(flightNumber, body);
            storageService.save(entity);
            syncToCache(entity);
            return ResponseEntity.ok(Map.of("flightNumber", flightNumber, "message", "Flight updated."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid data: " + e.getMessage());
        }
    }

    @DeleteMapping("/{flightNumber}")
    public ResponseEntity<?> deleteFlight(@PathVariable int flightNumber) {
        if (!storageService.exists(flightNumber)) return notFound(flightNumber);
        storageService.delete(flightNumber);
        removeFromCache(flightNumber);
        return ResponseEntity.ok(Map.of("message", "Flight " + flightNumber + " deleted."));
    }

    // ==================== HELPERS ====================

    private void updateStatus(int flightNumber, FlightReportDTO report) {
        if (report == null || report.getRecommendation() == null) return;
        RecommendationAction action = report.getRecommendation().getAction();
        FlightStatus newStatus = switch (action) {
            case CANCEL        -> FlightStatus.CANCELLED;
            case DELAY         -> FlightStatus.DELAYED;
            case REDIRECT      -> FlightStatus.REDIRECTED;
            case DEPART_ON_TIME -> FlightStatus.ON_TIME;
        };
        // Ažuriraj i u bazi i u cache-u
        storageService.updateStatus(flightNumber, newStatus);
        Flight cached = DroolsConsoleDemo.flightsRepository.get(flightNumber);
        if (cached != null) cached.setStatus(newStatus);
    }

    private Object[] resolve(int flightNumber) {
        Optional<FlightEntity> opt = storageService.findById(flightNumber);
        if (opt.isEmpty()) return null;
        FlightEntity fe = opt.get();
        return new Object[]{
            storageService.toFlight(fe),
            storageService.toWeather(fe),
            storageService.toRunway(fe),
            storageService.toAirport(fe),
            storageService.toCrew(fe),
            storageService.toAlarms(fe)
        };
    }

    private void syncToCache(FlightEntity fe) {
        int fn = fe.getFlightNumber();
        DroolsConsoleDemo.flightsRepository.put(fn, EntityMapper.toModel(fe));
        DroolsConsoleDemo.weatherRepository.put(fn, EntityMapper.toModel(fe.getWeather()));
        DroolsConsoleDemo.runwayRepository.put(fn, EntityMapper.toModel(fe.getRunway()));
        DroolsConsoleDemo.airportRepository.put(fn, EntityMapper.toModel(fe.getAirport()));
        DroolsConsoleDemo.crewRepository.put(fn, EntityMapper.toModel(fe.getCrew()));
        DroolsConsoleDemo.alarmsRepository.put(fn, EntityMapper.alarmsToModel(fe.getAlarms()));
    }

    private void removeFromCache(int flightNumber) {
        DroolsConsoleDemo.flightsRepository.remove(flightNumber);
        DroolsConsoleDemo.weatherRepository.remove(flightNumber);
        DroolsConsoleDemo.runwayRepository.remove(flightNumber);
        DroolsConsoleDemo.airportRepository.remove(flightNumber);
        DroolsConsoleDemo.crewRepository.remove(flightNumber);
        DroolsConsoleDemo.alarmsRepository.remove(flightNumber);
    }

    private FlightEntity buildEntity(int flightNumber, Map<String, Object> b) {
        Map<String, Object> acMap  = castMap(b.get("aircraft"));
        Map<String, Object> wMap   = castMap(b.get("weather"));
        Map<String, Object> rMap   = castMap(b.get("runway"));
        Map<String, Object> apMap  = castMap(b.get("airport"));
        Map<String, Object> cMap   = castMap(b.get("crew"));

        AircraftEntity ac = new AircraftEntity();
        ac.setAge(toInt(acMap.get("age")));
        ac.setNextServiceDate(LocalDate.parse((String) acMap.get("nextServiceDate")));
        ac.setFlightHoursSinceService(toInt(acMap.get("flightHoursSinceService")));
        ac.setCyclesSinceService(toInt(acMap.get("cyclesSinceService")));
        ac.setTotalFlightHours(toInt(acMap.get("totalFlightHours")));

        WeatherReportEntity w = new WeatherReportEntity();
        w.setWindSpeed(toInt(wMap.get("windSpeed")));
        w.setWindDirection(toInt(wMap.get("windDirection")));
        w.setCrosswind(toInt(wMap.get("crosswind")));
        w.setTailwind(toInt(wMap.get("tailwind")));
        w.setVisibility(toInt(wMap.get("visibility")));
        w.setTemperature(toInt(wMap.get("temperature")));
        w.setDewPoint(toInt(wMap.get("dewPoint")));
        w.setPrecipitationType(PrecipitationType.valueOf((String) wMap.get("precipitationType")));
        w.setPrecipitationIntensity(PrecipitationIntensity.valueOf((String) wMap.get("precipitationIntensity")));
        w.setIcingPresent(toBoolean(wMap.get("icingPresent")));

        RunwayEntity rw = new RunwayEntity();
        rw.setStatus(RunwayStatus.valueOf((String) rMap.get("status")));
        rw.setRwycc(toInt(rMap.get("rwycc")));
        rw.setRunwaysBeingDeiced(toBoolean(rMap.get("runwaysBeingDeiced")));
        rw.setDeicingComplete(toBoolean(rMap.get("deicingComplete")));

        AirportEntity ap = new AirportEntity();
        ap.setFreeGates(toInt(apMap.get("freeGates")));
        ap.setCapacity(toInt(apMap.get("capacity")));
        ap.setTotalRunways(toInt(apMap.get("totalRunways")));
        ap.setAvailableRunways(toInt(apMap.get("availableRunways")));
        ap.setRunwayHeading(toInt(apMap.get("runwayHeading")));
        ap.setRunwayLength(toInt(apMap.get("runwayLength")));
        ap.setLvtoCapability(toBoolean(apMap.get("lvtoCapability")));
        ap.setLvtoPermit(toBoolean(apMap.get("lvtoPermit")));
        ap.setSpecialPermit(toBoolean(apMap.get("specialPermit")));

        CrewEntity crew = new CrewEntity();
        crew.setFlightNumber(flightNumber);
        Object completeVal = cMap.containsKey("isComplete") ? cMap.get("isComplete") : cMap.get("complete");
        Object nightVal    = cMap.containsKey("isNightDuty") ? cMap.get("isNightDuty") : cMap.get("nightDuty");
        crew.setComplete(toBoolean(completeVal));
        crew.setNightDuty(toBoolean(nightVal));
        crew.setFdp(toDouble(cMap.get("fdp")));
        crew.setRestBeforeFlight(toDouble(cMap.get("restBeforeFlight")));
        crew.setSectorsToday(toInt(cMap.get("sectorsToday")));

        FlightEntity fe = new FlightEntity();
        fe.setFlightNumber(flightNumber);
        fe.setRoute((String) b.get("route"));
        fe.setCategory(FlightCategory.valueOf((String) b.get("category")));
        fe.setStatus(FlightStatus.valueOf((String) b.get("status")));
        fe.setPlannedDeparture(parseDateTime((String) b.get("plannedDeparture")));
        fe.setPlannedArrival(parseDateTime((String) b.get("plannedArrival")));
        fe.setPassengerCount(toInt(b.get("passengerCount")));
        fe.setDelayMinutes(toInt(b.getOrDefault("delayMinutes", 0)));
        fe.setHasReplacementAircraft(toBoolean(b.get("hasReplacementAircraft")));
        fe.setHasReplacementCrew(toBoolean(b.get("hasReplacementCrew")));
        fe.setAircraft(ac);
        fe.setWeather(w);
        fe.setRunway(rw);
        fe.setAirport(ap);
        fe.setCrew(crew);
        fe.setAlarms(new ArrayList<>());
        return fe;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> castMap(Object o) {
        return (Map<String, Object>) o;
    }

    private LocalDateTime parseDateTime(String s) {
        if (s == null) return null;
        if (s.length() == 16) s = s + ":00";
        return LocalDateTime.parse(s);
    }

    private int toInt(Object v) {
        if (v == null) return 0;
        if (v instanceof Integer) return (Integer) v;
        if (v instanceof Double)  return ((Double) v).intValue();
        if (v instanceof Long)    return ((Long) v).intValue();
        return Integer.parseInt(v.toString());
    }

    private double toDouble(Object v) {
        if (v == null) return 0;
        if (v instanceof Double)  return (Double) v;
        if (v instanceof Integer) return ((Integer) v).doubleValue();
        return Double.parseDouble(v.toString());
    }

    private boolean toBoolean(Object v) {
        if (v == null) return false;
        if (v instanceof Boolean) return (Boolean) v;
        return Boolean.parseBoolean(v.toString());
    }

    private ResponseEntity<String> notFound(int fn) {
        return ResponseEntity.status(404).body("Flight " + fn + " not found.");
    }
}
