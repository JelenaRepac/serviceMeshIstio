package com.airline.flightservice.service.impl;

import com.airline.flightservice.dto.FlightScheduleFilter;
import com.airline.flightservice.model.FlightSchedule;
import com.airline.flightservice.repository.FlightRepository;
import com.airline.flightservice.repository.FlightScheduleRepository;
import com.airline.flightservice.service.FlightScheduleService;
import com.airline.flightservice.specification.FlightScheduleSpecification;
import org.springframework.data.jpa.domain.Specification;
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

    @Override
    public List<FlightSchedule> searchSchedules(FlightScheduleFilter filter) {
        Specification<FlightSchedule> spec = FlightScheduleSpecification.build(filter);
        return flightScheduleRepository.findAll(spec);
    }

}
