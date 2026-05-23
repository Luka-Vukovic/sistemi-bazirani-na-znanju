package com.ftn.sbnz.model;

import java.io.Serializable;
import java.util.Objects;

public class Crew implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean isComplete;
    private int fdp;
    private int restBeforeFlight;
    private int sectorsToday;
    private boolean isNightDuty;

    public Crew() {}

    public Crew(boolean isComplete, int fdp, int restBeforeFlight,
                int sectorsToday, boolean isNightDuty) {
        this.isComplete = isComplete;
        this.fdp = fdp;
        this.restBeforeFlight = restBeforeFlight;
        this.sectorsToday = sectorsToday;
        this.isNightDuty = isNightDuty;
    }

    public boolean isComplete() { return isComplete; }
    public void setComplete(boolean complete) { isComplete = complete; }

    public int getFdp() { return fdp; }
    public void setFdp(int fdp) { this.fdp = fdp; }

    public int getRestBeforeFlight() { return restBeforeFlight; }
    public void setRestBeforeFlight(int restBeforeFlight) { this.restBeforeFlight = restBeforeFlight; }

    public int getSectorsToday() { return sectorsToday; }
    public void setSectorsToday(int sectorsToday) { this.sectorsToday = sectorsToday; }

    public boolean isNightDuty() { return isNightDuty; }
    public void setNightDuty(boolean nightDuty) { isNightDuty = nightDuty; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Crew crew = (Crew) o;
        return isComplete == crew.isComplete &&
                fdp == crew.fdp &&
                restBeforeFlight == crew.restBeforeFlight &&
                sectorsToday == crew.sectorsToday &&
                isNightDuty == crew.isNightDuty;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isComplete, fdp, restBeforeFlight, sectorsToday, isNightDuty);
    }

    @Override
    public String toString() {
        return "Crew{" +
                "isComplete=" + isComplete +
                ", fdp=" + fdp +
                ", restBeforeFlight=" + restBeforeFlight +
                ", sectorsToday=" + sectorsToday +
                ", isNightDuty=" + isNightDuty +
                '}';
    }
}
