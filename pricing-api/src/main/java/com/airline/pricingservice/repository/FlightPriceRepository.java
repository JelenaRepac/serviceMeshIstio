package com.airline.pricingservice.repository;

import com.airlines.airlinesharedmodule.FlightPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlightPriceRepository extends JpaRepository<FlightPrice, Long> {
    List<FlightPrice> findByFlightScheduleId(Long flightScheduleId);
}
