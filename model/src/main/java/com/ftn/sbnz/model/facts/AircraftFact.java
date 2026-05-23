package com.ftn.sbnz.model.facts;

public abstract class AircraftFact extends Fact {
    private static final long serialVersionUID = 1L;

    public AircraftFact() {}
    public AircraftFact(int flightNumber) { super(flightNumber); }
}
