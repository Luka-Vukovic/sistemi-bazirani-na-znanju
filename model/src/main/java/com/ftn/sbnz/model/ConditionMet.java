package com.ftn.sbnz.model;

import java.io.Serializable;
import java.util.Objects;

public class ConditionMet implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private boolean isSatisfied;
    private String reason; // <-- Dodato polje za opis greške

    public ConditionMet() {}

    public ConditionMet(String name, boolean isSatisfied) {
        this.name = name;
        this.isSatisfied = isSatisfied;
        this.reason = "";
    }

    public ConditionMet(String name, boolean isSatisfied, String reason) {
        this.name = name;
        this.isSatisfied = isSatisfied;
        this.reason = reason;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isSatisfied() { return isSatisfied; }
    public void setSatisfied(boolean satisfied) { isSatisfied = satisfied; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConditionMet that = (ConditionMet) o;
        return isSatisfied == that.isSatisfied &&
                Objects.equals(name, that.name) &&
                Objects.equals(reason, that.reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, isSatisfied, reason);
    }

    @Override
    public String toString() {
        return "ConditionMet{" +
                "name='" + name + '\'' +
                ", isSatisfied=" + isSatisfied +
                ", reason='" + reason + '\'' +
                '}';
    }
}