package com.airline.flightservice.repository;

import com.airline.flightservice.model.FlightScheduleSeatInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightScheduleSeatInformationRepository extends JpaRepository<FlightScheduleSeatInformation, Long> {
    List<FlightScheduleSeatInformation> findByFlightScheduleId(Long flightScheduleId);
    List<FlightScheduleSeatInformation> findByBookingStatus(Boolean bookingStatus);
    List<FlightScheduleSeatInformation> findBySeatType(String seatType);


}
