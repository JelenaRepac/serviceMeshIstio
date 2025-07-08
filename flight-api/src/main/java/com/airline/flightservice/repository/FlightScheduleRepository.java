package com.airline.flightservice.repository;

import com.airlines.airlinesharedmodule.FlightSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlightScheduleRepository extends JpaRepository<FlightSchedule, Long>, JpaSpecificationExecutor<FlightSchedule> {
    List<FlightSchedule> findByFlightInformationId(Long flightId);

    @Query("SELECT fs FROM FlightSchedule fs " +
            "LEFT JOIN FETCH fs.flightInformation " +
            "WHERE fs.id = :id")
    Optional<FlightSchedule> findByIdWithAllRelations(@Param("id") Long id);

}