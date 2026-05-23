package com.ftn.sbnz.model;

import com.ftn.sbnz.model.enums.RecommendationAction;

import java.io.Serializable;
import java.util.Objects;

public class Recommendation implements Serializable {

    private static final long serialVersionUID = 1L;

    private RecommendationAction action;
    private String reason;

    public Recommendation() {}

    public Recommendation(RecommendationAction action, String reason) {
        this.action = action;
        this.reason = reason;
    }

    public RecommendationAction getAction() { return action; }
    public void setAction(RecommendationAction action) { this.action = action; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recommendation that = (Recommendation) o;
        return action == that.action &&
                Objects.equals(reason, that.reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(action, reason);
    }

    @Override
    public String toString() {
        return "Recommendation{" +
                "action=" + action +
                ", reason='" + reason + '\'' +
                '}';
    }
}
