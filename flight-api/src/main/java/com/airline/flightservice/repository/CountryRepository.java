package com.airline.flightservice.repository;

import com.airline.flightservice.model.Country;
import com.airline.flightservice.model.FlightInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {

}
