package com.airline.authservice.repository;

import com.airline.authservice.model.FlightInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<FlightInformation, Long> {
}
