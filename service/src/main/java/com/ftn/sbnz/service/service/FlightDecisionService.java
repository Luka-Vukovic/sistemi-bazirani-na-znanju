package com.ftn.sbnz.service.service;

import com.ftn.sbnz.model.*;
import com.ftn.sbnz.model.dto.FlightReportDTO;
import com.ftn.sbnz.model.Recommendation;
import com.ftn.sbnz.service.util.ConsoleAgendaEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class FlightDecisionService {

    private static final Logger logger = LoggerFactory.getLogger(FlightDecisionService.class);

    @Autowired
    private KieContainer kieContainer;

    public FlightReportDTO evaluateFlight(Flight flight, WeatherReport weather, Runway runway) {
        logger.info("====== STARTING DROOLS EVALUATION FOR FLIGHT {} ======", flight.getFlightNumber());

        KieSession kSession = kieContainer.newKieSession("ksession-rules");
        
        kSession.addEventListener(new ConsoleAgendaEventListener());

        kSession.insert(flight);
        kSession.insert(weather);
        kSession.insert(runway);

        kSession.fireAllRules();

        Recommendation recommendation = null;
        Collection<?> recommendations = kSession.getObjects(o -> o instanceof Recommendation);
        if (!recommendations.isEmpty()) {
            recommendation = (Recommendation) recommendations.iterator().next();
            logger.info("DECISION EVALUATED: {} FOR FLIGHT {}", recommendation.getAction(), flight.getFlightNumber());
        } else {
            logger.error("SYSTEM ERROR: Failed to evaluate a recommendation.");
        }

        kSession.dispose();
        logger.info("====== COMPLETED EVALUATION FOR FLIGHT {} ======\n", flight.getFlightNumber());

        return new FlightReportDTO(flight, weather, runway, recommendation);
    }
}