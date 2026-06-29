package com.ftn.sbnz.service.entity;

import com.ftn.sbnz.model.enums.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "technical_alarm")
public class TechnicalAlarmEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Dodata eksplicitna anotacija da Hibernate zna tačan naziv kolone
    @Column(name = "flight_number")
    private int flightNumber;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    private String component;

    @Enumerated(EnumType.STRING)
    private AlarmSeverity severity;

    private LocalDateTime reportedAt;

    @Enumerated(EnumType.STRING)
    private AlarmStatus status;

    @Enumerated(EnumType.STRING)
    private MelCategory melCategory;

    public TechnicalAlarmEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getFlightNumber() { return flightNumber; }
    public void setFlightNumber(int v) { this.flightNumber = v; }
    public AlarmType getAlarmType() { return alarmType; }
    public void setAlarmType(AlarmType v) { this.alarmType = v; }
    public String getComponent() { return component; }
    public void setComponent(String v) { this.component = v; }
    public AlarmSeverity getSeverity() { return severity; }
    public void setSeverity(AlarmSeverity v) { this.severity = v; }
    public LocalDateTime getReportedAt() { return reportedAt; }
    public void setReportedAt(LocalDateTime v) { this.reportedAt = v; }
    public AlarmStatus getStatus() { return status; }
    public void setStatus(AlarmStatus v) { this.status = v; }
    public MelCategory getMelCategory() { return melCategory; }
    public void setMelCategory(MelCategory v) { this.melCategory = v; }
}