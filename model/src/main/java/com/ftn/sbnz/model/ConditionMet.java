package com.ftn.sbnz.model;

import java.io.Serializable;
import java.util.Objects;

public class ConditionMet implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private boolean isSatisfied;

    public ConditionMet() {}

    public ConditionMet(String name, boolean isSatisfied) {
        this.name = name;
        this.isSatisfied = isSatisfied;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isSatisfied() { return isSatisfied; }
    public void setSatisfied(boolean satisfied) { isSatisfied = satisfied; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConditionMet that = (ConditionMet) o;
        return isSatisfied == that.isSatisfied &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, isSatisfied);
    }

    @Override
    public String toString() {
        return "ConditionMet{" +
                "name='" + name + '\'' +
                ", isSatisfied=" + isSatisfied +
                '}';
    }
}
