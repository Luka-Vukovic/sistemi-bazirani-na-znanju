package com.ftn.sbnz.model;

import com.ftn.sbnz.model.enums.AlarmSeverity;
import com.ftn.sbnz.model.enums.AlarmStatus;
import com.ftn.sbnz.model.enums.AlarmType;
import com.ftn.sbnz.model.enums.MelCategory;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class TechnicalAlarm implements Serializable {

    private static final long serialVersionUID = 1L;

    private String aircraftId;
    private AlarmType alarmType;
    private String component;
    private AlarmSeverity severity;
    private LocalDateTime reportedAt;
    private AlarmStatus status;
    private MelCategory melCategory;

    public TechnicalAlarm() {}

    public TechnicalAlarm(String aircraftId, AlarmType alarmType, String component,
                          AlarmSeverity severity, LocalDateTime reportedAt,
                          AlarmStatus status, MelCategory melCategory) {
        this.aircraftId = aircraftId;
        this.alarmType = alarmType;
        this.component = component;
        this.severity = severity;
        this.reportedAt = reportedAt;
        this.status = status;
        this.melCategory = melCategory;
    }

    public String getAircraftId() { return aircraftId; }
    public void setAircraftId(String aircraftId) { this.aircraftId = aircraftId; }

    public AlarmType getAlarmType() { return alarmType; }
    public void setAlarmType(AlarmType alarmType) { this.alarmType = alarmType; }

    public String getComponent() { return component; }
    public void setComponent(String component) { this.component = component; }

    public AlarmSeverity getSeverity() { return severity; }
    public void setSeverity(AlarmSeverity severity) { this.severity = severity; }

    public LocalDateTime getReportedAt() { return reportedAt; }
    public void setReportedAt(LocalDateTime reportedAt) { this.reportedAt = reportedAt; }

    public AlarmStatus getStatus() { return status; }
    public void setStatus(AlarmStatus status) { this.status = status; }

    public MelCategory getMelCategory() { return melCategory; }
    public void setMelCategory(MelCategory melCategory) { this.melCategory = melCategory; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TechnicalAlarm that = (TechnicalAlarm) o;
        return Objects.equals(aircraftId, that.aircraftId) &&
                alarmType == that.alarmType &&
                Objects.equals(component, that.component) &&
                severity == that.severity &&
                Objects.equals(reportedAt, that.reportedAt) &&
                status == that.status &&
                melCategory == that.melCategory;
    }

    @Override
    public int hashCode() {
        return Objects.hash(aircraftId, alarmType, component, severity, reportedAt, status, melCategory);
    }

    @Override
    public String toString() {
        return "TechnicalAlarm{" +
                "aircraftId='" + aircraftId + '\'' +
                ", alarmType=" + alarmType +
                ", component='" + component + '\'' +
                ", severity=" + severity +
                ", reportedAt=" + reportedAt +
                ", status=" + status +
                ", melCategory=" + melCategory +
                '}';
    }
}
