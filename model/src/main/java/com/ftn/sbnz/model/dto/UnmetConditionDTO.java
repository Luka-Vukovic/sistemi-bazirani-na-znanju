package com.ftn.sbnz.model.dto;

import java.io.Serializable;

public class UnmetConditionDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String condition;
    private String reason;

    public UnmetConditionDTO() {}

    public UnmetConditionDTO(String condition, String reason) {
        this.condition = condition;
        this.reason = reason;
    }

    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}