package com.ftn.sbnz.model;

import java.io.Serializable;
import java.util.Objects;

public class Condition implements Serializable {

    private static final long serialVersionUID = 1L;

    private String parent;
    private String child;

    public Condition() {}

    public Condition(String parent, String child) {
        this.parent = parent;
        this.child = child;
    }

    public String getParent() { return parent; }
    public void setParent(String parent) { this.parent = parent; }

    public String getChild() { return child; }
    public void setChild(String child) { this.child = child; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Condition that = (Condition) o;
        return Objects.equals(parent, that.parent) &&
                Objects.equals(child, that.child);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, child);
    }

    @Override
    public String toString() {
        return "Condition{" +
                "parent='" + parent + '\'' +
                ", child='" + child + '\'' +
                '}';
    }
}
