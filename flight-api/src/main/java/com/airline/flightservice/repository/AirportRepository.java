package com.airline.flightservice.repository;

import com.airline.flightservice.model.Airport;
import com.airline.flightservice.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Integer> {

    List<Airport> findByCountry(String countryCode);
}
