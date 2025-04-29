package com.airline.authservice.service.impl;

import com.airline.authservice.model.FlightSchedule;
import com.airline.authservice.repository.FlightRepository;
import com.airline.authservice.repository.FlightScheduleRepository;
import com.airline.authservice.service.FlightScheduleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightScheduleServiceImpl implements FlightScheduleService {

    private final FlightScheduleRepository flightScheduleRepository;
    private final FlightRepository flightRepository;

    public FlightScheduleServiceImpl(FlightScheduleRepository flightScheduleRepository,
                                     FlightRepository flightRepository) {
        this.flightScheduleRepository = flightScheduleRepository;
        this.flightRepository = flightRepository;
    }

    @Override
    public List<FlightSchedule> getAllFlightSchedules() {

        return flightScheduleRepository.findAll();
    }

    @Override
    public FlightSchedule addFlightSchedule(FlightSchedule flightSchedule) {

//        FlightInformation flightInformation= flightRepository.findById(flightSchedule.getFlightInformation().getId()).get();
//
//        flightSchedule.setFlightInformation(flightInformation);

//        FlightSchedule flightSchedule = flightScheduleMapper.mapToFlightSchedule(flightScheduleInputDto);
        FlightSchedule savedFlightSchedule = flightScheduleRepository.save(flightSchedule);
        return savedFlightSchedule;
    }
}
