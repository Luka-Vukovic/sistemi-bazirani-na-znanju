package com.ftn.sbnz.model.events;

import com.ftn.sbnz.model.enums.AlarmSeverity;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

import java.io.Serializable;

@Role(Role.Type.EVENT)
@Timestamp("timestamp")
public class TechnicalAlarmEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private int flightNumber;
    private long timestamp;
    private AlarmSeverity severity;

    public TechnicalAlarmEvent() {}

    public TechnicalAlarmEvent(int flightNumber, AlarmSeverity severity) {
        this.flightNumber = flightNumber;
        this.timestamp = System.currentTimeMillis();
        this.severity = severity;
    }

    public int getFlightNumber() { return flightNumber; }
    public void setFlightNumber(int flightNumber) { this.flightNumber = flightNumber; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public AlarmSeverity getSeverity() { return severity; }
    public void setSeverity(AlarmSeverity severity) { this.severity = severity; }
}