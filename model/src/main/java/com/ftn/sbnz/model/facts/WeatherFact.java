package com.ftn.sbnz.model.facts;

public abstract class WeatherFact extends Fact {
    private static final long serialVersionUID = 1L;

    public WeatherFact() {}
    public WeatherFact(int flightNumber) { super(flightNumber); }
}
