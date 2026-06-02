package com.ftn.sbnz.service.service;

import com.ftn.sbnz.model.Flight;
import com.ftn.sbnz.model.events.TechnicalAlarmEvent;
import com.ftn.sbnz.model.events.WeatherUpdateEvent;
import com.ftn.sbnz.model.enums.AlarmSeverity;
import com.ftn.sbnz.model.facts.TechnicalIncident;
import com.ftn.sbnz.model.facts.WeatherDeteriorationAlert;
import com.ftn.sbnz.service.util.ConsoleAgendaEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class CepService {

    @Autowired
    private KieContainer kieContainer;

    // Jedna dugotrajna sesija po letu
    private final Map<Integer, KieSession> sessions = new HashMap<>();

    public KieSession getOrCreateSession(int flightNumber, Flight flight) {
        if (!sessions.containsKey(flightNumber)) {
            KieSession kSession = kieContainer.newKieSession("ksession-rules");
            kSession.addEventListener(new ConsoleAgendaEventListener());
            kSession.insert(flight);
            sessions.put(flightNumber, kSession);
        }
        return sessions.get(flightNumber);
    }

    public void sendWeatherEvent(int flightNumber, Flight flight,
                                  int windSpeed, int visibility, int temperature) {
        KieSession kSession = getOrCreateSession(flightNumber, flight);
        WeatherUpdateEvent event = new WeatherUpdateEvent(flightNumber, windSpeed, visibility, temperature);
        kSession.insert(event);
        kSession.fireAllRules();
    }

    public void sendAlarmEvent(int flightNumber, Flight flight, AlarmSeverity severity) {
        KieSession kSession = getOrCreateSession(flightNumber, flight);
        TechnicalAlarmEvent event = new TechnicalAlarmEvent(flightNumber, severity);
        kSession.insert(event);
        kSession.fireAllRules();
    }

    public boolean hasWeatherDeteriorationAlert(int flightNumber) {
        KieSession kSession = sessions.get(flightNumber);
        if (kSession == null) return false;
        Collection<?> alerts = kSession.getObjects(o ->
                o instanceof WeatherDeteriorationAlert &&
                ((WeatherDeteriorationAlert) o).getFlightNumber() == flightNumber);
        return !alerts.isEmpty();
    }

    public boolean hasTechnicalIncident(int flightNumber) {
        KieSession kSession = sessions.get(flightNumber);
        if (kSession == null) return false;
        Collection<?> incidents = kSession.getObjects(o ->
                o instanceof TechnicalIncident &&
                ((TechnicalIncident) o).getFlightNumber() == flightNumber);
        return !incidents.isEmpty();
    }

    @PreDestroy
    public void cleanup() {
        sessions.values().forEach(KieSession::dispose);
        sessions.clear();
    }
}