package com.ftn.sbnz.service.entity;

import com.ftn.sbnz.model.enums.RunwayStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "runway")
public class RunwayEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RunwayStatus status;

    private int rwycc;
    private boolean runwaysBeingDeiced;
    private boolean deicingComplete;

    public RunwayEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public RunwayStatus getStatus() { return status; }
    public void setStatus(RunwayStatus v) { this.status = v; }
    public int getRwycc() { return rwycc; }
    public void setRwycc(int v) { this.rwycc = v; }
    public boolean isRunwaysBeingDeiced() { return runwaysBeingDeiced; }
    public void setRunwaysBeingDeiced(boolean v) { this.runwaysBeingDeiced = v; }
    public boolean isDeicingComplete() { return deicingComplete; }
    public void setDeicingComplete(boolean v) { this.deicingComplete = v; }
}
