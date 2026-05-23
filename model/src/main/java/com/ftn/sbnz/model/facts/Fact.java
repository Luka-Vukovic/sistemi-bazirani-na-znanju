package com.ftn.sbnz.model.facts;

import java.io.Serializable;
import java.util.Objects;

public abstract class Fact implements Serializable {

    private static final long serialVersionUID = 1L;

    private int flightNumber;

    public Fact() {}

    public Fact(int flightNumber) {
        this.flightNumber = flightNumber;
    }

    public int getFlightNumber() { return flightNumber; }
    public void setFlightNumber(int flightNumber) { this.flightNumber = flightNumber; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fact fact = (Fact) o;
        return flightNumber == fact.flightNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(flightNumber);
    }
}
