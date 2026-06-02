package com.ftn.sbnz.service.service;

import com.ftn.sbnz.model.*;
import com.ftn.sbnz.model.dto.FlightReportDTO;
import com.ftn.sbnz.service.util.ConsoleAgendaEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.kie.api.runtime.rule.Variable; // <-- DODATO: Neophodno za prosleđivanje varijable u query
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class FlightDecisionService {

    private static final Logger logger = LoggerFactory.getLogger(FlightDecisionService.class);

    @Autowired
    private KieContainer kieContainer;

    private KieSession buildAndFire(Flight flight, WeatherReport weather, Runway runway,
                                    Airport airport, Crew crew, List<TechnicalAlarm> alarms) {
        KieSession kSession = kieContainer.newKieSession("ksession-rules");
        kSession.addEventListener(new ConsoleAgendaEventListener());

        kSession.insert(flight);
        kSession.insert(weather);
        kSession.insert(runway);
        kSession.insert(airport);
        kSession.insert(crew);
        for (TechnicalAlarm alarm : alarms) {
            kSession.insert(alarm);
        }

        kSession.fireAllRules();
        return kSession;
    }

    private Recommendation extractRecommendation(KieSession kSession, int flightNumber) {
        Collection<?> recs = kSession.getObjects(o -> o instanceof Recommendation);
        if (!recs.isEmpty()) {
            Recommendation rec = (Recommendation) recs.iterator().next();
            logger.info("DECISION: {} FOR FLIGHT {}", rec.getAction(), flightNumber);
            return rec;
        }
        logger.error("No recommendation generated for flight {}", flightNumber);
        return null;
    }

    public FlightReportDTO evaluateFlight(Flight flight, WeatherReport weather, Runway runway,
                                          Airport airport, Crew crew, List<TechnicalAlarm> alarms) {
        logger.info("=== FORWARD CHAINING: Flight {} ===", flight.getFlightNumber());

        KieSession kSession = buildAndFire(flight, weather, runway, airport, crew, alarms);
        Recommendation recommendation = extractRecommendation(kSession, flight.getFlightNumber());
        kSession.dispose();

        return new FlightReportDTO(flight, weather, runway, recommendation);
    }

    public FlightReportDTO evaluateFlightWithConditions(Flight flight, WeatherReport weather,
                                                         Runway runway, Airport airport,
                                                         Crew crew, List<TechnicalAlarm> alarms) {
        logger.info("=== FORWARD + BACKWARD: Flight {} ===", flight.getFlightNumber());

        KieSession kSession = buildAndFire(flight, weather, runway, airport, crew, alarms);
        Recommendation recommendation = extractRecommendation(kSession, flight.getFlightNumber());

        List<String> unmetConditions = new ArrayList<>();
        
        // IZMENA: Prosleđena su 2 argumenta. "takeoffAllowed" kao ulazni ($parent) 
        // i Variable.v kao izlazni ($child) koji Drools treba da poveže i vrati.
        QueryResults results = kSession.getQueryResults("unmetConditions", "takeoffAllowed", Variable.v);

        for (QueryResultsRow row : results) {
            String unmet = (String) row.get("$child");
            if (!unmetConditions.contains(unmet)) {
                unmetConditions.add(unmet);
            }
        }

        logger.info("Unmet conditions for flight {}: {}", flight.getFlightNumber(), unmetConditions);
        kSession.dispose();

        return new FlightReportDTO(flight, weather, runway, recommendation, unmetConditions);
    }
}