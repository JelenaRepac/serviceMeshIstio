package com.airline.flightservice.service;

import com.airline.flightservice.dto.FlightScheduleFilter;
import com.airlines.airlinesharedmodule.FlightSchedule;

import java.util.List;

public interface FlightScheduleService {
    List<FlightSchedule> getAllFlightSchedules();
    FlightSchedule findById(Long id);


    FlightSchedule addFlightSchedule(FlightSchedule flightScheduleInputDto);

    List<FlightSchedule> searchSchedules(FlightScheduleFilter filter);
}
