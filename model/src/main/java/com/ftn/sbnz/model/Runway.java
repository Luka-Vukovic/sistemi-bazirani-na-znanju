package com.ftn.sbnz.model;

import com.ftn.sbnz.model.enums.RunwayStatus;

import java.io.Serializable;
import java.util.Objects;

public class Runway implements Serializable {

    private static final long serialVersionUID = 1L;

    private RunwayStatus status;
    private int rwycc;
    private boolean runwaysBeingDeiced;
    private boolean deicingComplete;

    public Runway() {}

    public Runway(RunwayStatus status, int rwycc, boolean runwaysBeingDeiced, boolean deicingComplete) {
        this.status = status;
        this.rwycc = rwycc;
        this.runwaysBeingDeiced = runwaysBeingDeiced;
        this.deicingComplete = deicingComplete;
    }

    public RunwayStatus getStatus() { return status; }
    public void setStatus(RunwayStatus status) { this.status = status; }

    public int getRwycc() { return rwycc; }
    public void setRwycc(int rwycc) { this.rwycc = rwycc; }

    public boolean isRunwaysBeingDeiced() { return runwaysBeingDeiced; }
    public void setRunwaysBeingDeiced(boolean runwaysBeingDeiced) { this.runwaysBeingDeiced = runwaysBeingDeiced; }

    public boolean isDeicingComplete() { return deicingComplete; }
    public void setDeicingComplete(boolean deicingComplete) { this.deicingComplete = deicingComplete; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Runway runway = (Runway) o;
        return rwycc == runway.rwycc &&
                runwaysBeingDeiced == runway.runwaysBeingDeiced &&
                deicingComplete == runway.deicingComplete &&
                status == runway.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, rwycc, runwaysBeingDeiced, deicingComplete);
    }

    @Override
    public String toString() {
        return "Runway{" +
                "status=" + status +
                ", rwycc=" + rwycc +
                ", runwaysBeingDeiced=" + runwaysBeingDeiced +
                ", deicingComplete=" + deicingComplete +
                '}';
    }
}
