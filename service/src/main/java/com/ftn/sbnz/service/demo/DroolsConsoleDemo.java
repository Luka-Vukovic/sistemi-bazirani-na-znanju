package com.ftn.sbnz.service.demo;

import java.time.LocalDateTime;
import java.util.Collection;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.ftn.sbnz.model.*;
import com.ftn.sbnz.model.enums.*;
import com.ftn.sbnz.model.facts.*;
import com.ftn.sbnz.service.util.ConsoleAgendaEventListener;

public class DroolsConsoleDemo {

    public static void runDemo() {

        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();

        System.out.println("==================================================");
        System.out.println("          FLIGHT DECISION SUPPORT SYSTEM          ");
        System.out.println("==================================================\n");

        simulateFlightCancellation(kContainer);
        simulateFlightDelay(kContainer);
        simulateRegularFlight(kContainer);
    }

    /**
     * SCENARIO 1: CANCEL
     * Tailwind > 19
     * Visibility < 75
     * Runway critical (rwycc <= 1)
     */
    private static void simulateFlightCancellation(KieContainer kContainer) {
        System.out.println("--- [SCENARIO 1: CRITICAL CONDITIONS - CANCELLATION] ---");
        
        KieSession kSession = kContainer.newKieSession("ksession-rules");
        kSession.addEventListener(new ConsoleAgendaEventListener());

        int flightNum = 101;

        Flight flight = new Flight();
        flight.setFlightNumber(flightNum);
        flight.setStatus(FlightStatus.SCHEDULED);
        flight.setPlannedDeparture(LocalDateTime.now().plusHours(2));
        flight.setHasReplacementAircraft(false);

        WeatherReport weather = new WeatherReport();
        weather.setTailwind(25);       // > 19 -> "Meteo - Tailwind Component"
        weather.setVisibility(50);     // < 75 -> "Meteo - Zero Visibility" -> TakeoffForbidden

        Runway runway = new Runway();
        runway.setRwycc(1);            // <= 1 -> "Infrastructure - Runway Critical State" -> RunwayProblem
        runway.setStatus(RunwayStatus.OPEN);

        kSession.insert(flight);
        kSession.insert(weather);
        kSession.insert(runway);

        kSession.fireAllRules();

        printRecommendation(kSession, flightNum);
        kSession.dispose(); 
        
        System.out.println("\n--------------------------------------------------\n");
    }

    /**
     * SCENARIO 2: DELAY
     * Frost
     * temp <= 0
     * dewPoint >= temp
     * deicingComplete == false
     */
    private static void simulateFlightDelay(KieContainer kContainer) {
        System.out.println("--- [SCENARIO 2: OPERATIONAL HAZARD - DELAY] ---");
        
        KieSession kSession = kContainer.newKieSession("ksession-rules");
        kSession.addEventListener(new ConsoleAgendaEventListener());

        int flightNum = 202;

        Flight flight = new Flight();
        flight.setFlightNumber(flightNum);
        flight.setStatus(FlightStatus.SCHEDULED);
        flight.setPlannedDeparture(LocalDateTime.now().plusHours(4));
        
        WeatherReport weather = new WeatherReport();
        weather.setTemperature(-5);
        weather.setDewPoint(-5);       // dewPoint >= temperature -> "Meteo - Frost Risk" -> SurfacesContaminated
        weather.setTailwind(5);
        weather.setVisibility(5000); 

        Runway runway = new Runway();
        runway.setRwycc(4);
        runway.setDeicingComplete(false); // deicingComplete = flase -> "Infrastructure - Runway Risky" -> RunwayDifficult

        kSession.insert(flight);
        kSession.insert(weather);
        kSession.insert(runway);

        kSession.fireAllRules();

        printRecommendation(kSession, flightNum);
        kSession.dispose();
        
        System.out.println("\n--------------------------------------------------\n");
    }

    /**
     * SCENARIO 3: DEPART_ON_TIME.
     */
    private static void simulateRegularFlight(KieContainer kContainer) {
        System.out.println("--- [SCENARIO 3: IDEAL CONDITIONS - DEPART ON TIME] ---");
        
        KieSession kSession = kContainer.newKieSession("ksession-rules");
        kSession.addEventListener(new ConsoleAgendaEventListener());

        int flightNum = 303;

        Flight flight = new Flight();
        flight.setFlightNumber(flightNum);
        flight.setStatus(FlightStatus.SCHEDULED);
        flight.setPlannedDeparture(LocalDateTime.now().plusHours(5));
        
        WeatherReport weather = new WeatherReport();
        weather.setTailwind(5);
        weather.setCrosswind(10);
        weather.setVisibility(10000);
        weather.setTemperature(15);
        weather.setDewPoint(5);

        Runway runway = new Runway();
        runway.setRwycc(6);
        runway.setStatus(RunwayStatus.OPEN);
        runway.setDeicingComplete(true);

        kSession.insert(flight);
        kSession.insert(weather);
        kSession.insert(runway);

        kSession.fireAllRules();

        printRecommendation(kSession, flightNum);
        kSession.dispose();
        
        System.out.println("\n==================================================");
    }

    private static void printRecommendation(KieSession kSession, int flightNum) {
        Collection<?> recommendations = kSession.getObjects(o -> o instanceof Recommendation);
        
        if (!recommendations.isEmpty()) {
            Recommendation rec = (Recommendation) recommendations.iterator().next();
            System.out.println("\n[FINAL SYSTEM RECOMMENDATION FOR FLIGHT " + flightNum + "]:");
            System.out.println("  ACTION: " + rec.getAction());
            System.out.println("  REASON: " + rec.getReason());
        } else {
            System.out.println("\n[SYSTEM ERROR]: Engine failed to evaluate a recommendation.");
        }
    }
}