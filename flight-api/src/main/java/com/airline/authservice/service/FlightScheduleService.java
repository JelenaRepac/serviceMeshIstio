package com.airline.authservice.service;

import com.airline.authservice.model.FlightSchedule;

import java.util.List;

public interface FlightScheduleService {
    List<FlightSchedule> getAllFlightSchedules();

    FlightSchedule addFlightSchedule(FlightSchedule flightScheduleInputDto);
}
