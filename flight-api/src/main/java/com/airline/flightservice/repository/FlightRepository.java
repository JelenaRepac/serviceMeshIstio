package com.airline.flightservice.repository;

import com.airline.flightservice.model.Flight;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends PagingAndSortingRepository<Flight, Long> {
}
