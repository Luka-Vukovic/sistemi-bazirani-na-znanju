package com.ftn.sbnz.service.repository;

import com.ftn.sbnz.service.entity.FlightEntity;
import com.ftn.sbnz.model.enums.FlightStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<FlightEntity, Integer> {
    List<FlightEntity> findByPlannedDepartureBetween(LocalDateTime from, LocalDateTime to);
    List<FlightEntity> findByStatus(FlightStatus status);
}
