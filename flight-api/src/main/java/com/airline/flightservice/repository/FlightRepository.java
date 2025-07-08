package com.airline.flightservice.repository;

import com.airlines.airlinesharedmodule.FlightInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<FlightInformation, Long> {
}
