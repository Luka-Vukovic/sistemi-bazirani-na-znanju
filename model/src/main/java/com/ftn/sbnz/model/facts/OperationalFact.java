package com.ftn.sbnz.model.facts;

public abstract class OperationalFact extends Fact {
    private static final long serialVersionUID = 1L;

    public OperationalFact() {}
    public OperationalFact(int flightNumber) { super(flightNumber); }
}
