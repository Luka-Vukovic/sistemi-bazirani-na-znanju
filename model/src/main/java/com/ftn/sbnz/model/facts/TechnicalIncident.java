package com.ftn.sbnz.model.facts;

import java.util.Objects;

public class TechnicalIncident extends AircraftFact {

    private static final long serialVersionUID = 1L;

    private String aircraftId;

    public TechnicalIncident() {}

    public TechnicalIncident(String aircraftId) {
        this.aircraftId = aircraftId;
    }

    public String getAircraftId() { return aircraftId; }
    public void setAircraftId(String aircraftId) { this.aircraftId = aircraftId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TechnicalIncident that = (TechnicalIncident) o;
        return Objects.equals(aircraftId, that.aircraftId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aircraftId);
    }

    @Override
    public String toString() {
        return "TechnicalIncident{aircraftId='" + aircraftId + "'}";
    }
}
