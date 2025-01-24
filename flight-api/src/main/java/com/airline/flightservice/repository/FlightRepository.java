package com.airline.flightservice.repository;

import com.airline.flightservice.model.FlightInformation;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends PagingAndSortingRepository<FlightInformation, Long> {
}
