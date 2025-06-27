package com.airline.flightservice.repository;

import com.airline.flightservice.model.FlightSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightScheduleRepository extends JpaRepository<FlightSchedule, Long>, JpaSpecificationExecutor<FlightSchedule> {
    List<FlightSchedule> findByFlightInformationId(Long flightId);
}