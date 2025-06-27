package com.airline.flightservice.service;

import com.airline.flightservice.dto.FlightScheduleFilter;
import com.airline.flightservice.model.FlightSchedule;

import java.util.List;

public interface FlightScheduleService {
    List<FlightSchedule> getAllFlightSchedules();

    FlightSchedule addFlightSchedule(FlightSchedule flightScheduleInputDto);

    List<FlightSchedule> searchSchedules(FlightScheduleFilter filter);
}
