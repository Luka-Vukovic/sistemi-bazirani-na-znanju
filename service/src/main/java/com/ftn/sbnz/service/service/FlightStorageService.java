package com.ftn.sbnz.service.service;

import com.ftn.sbnz.model.*;
import com.ftn.sbnz.model.enums.FlightStatus;
import com.ftn.sbnz.service.entity.*;
import com.ftn.sbnz.service.repository.FlightRepository;
import com.ftn.sbnz.service.util.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class FlightStorageService {

    @Autowired
    private FlightRepository flightRepository;

    // ── Read ──

    public List<FlightEntity> findAll() {
        return flightRepository.findAll();
    }

    public List<FlightEntity> findByDate(LocalDate date) {
        LocalDateTime from = date.atStartOfDay();
        LocalDateTime to   = date.atTime(23, 59, 59);
        return flightRepository.findByPlannedDepartureBetween(from, to);
    }

    public Optional<FlightEntity> findById(int flightNumber) {
        return flightRepository.findById(flightNumber);
    }

    public boolean exists(int flightNumber) {
        return flightRepository.existsById(flightNumber);
    }

    // ── Write ──

    @Transactional
    public FlightEntity save(FlightEntity entity) {
        return flightRepository.save(entity);
    }

    @Transactional
    public void delete(int flightNumber) {
        flightRepository.deleteById(flightNumber);
    }

    @Transactional
    public FlightEntity updateStatus(int flightNumber, FlightStatus status) {
        Optional<FlightEntity> opt = flightRepository.findById(flightNumber);
        if (opt.isEmpty()) return null;
        FlightEntity e = opt.get();
        e.setStatus(status);
        return flightRepository.save(e);
    }

    // ── Convert to Drools model objects ──

    public Flight toFlight(FlightEntity e) {
        return EntityMapper.toModel(e);
    }

    public WeatherReport toWeather(FlightEntity e) {
        return EntityMapper.toModel(e.getWeather());
    }

    public Runway toRunway(FlightEntity e) {
        return EntityMapper.toModel(e.getRunway());
    }

    public Airport toAirport(FlightEntity e) {
        return EntityMapper.toModel(e.getAirport());
    }

    public Crew toCrew(FlightEntity e) {
        return EntityMapper.toModel(e.getCrew());
    }

    public List<TechnicalAlarm> toAlarms(FlightEntity e) {
        return EntityMapper.alarmsToModel(e.getAlarms());
    }
}
