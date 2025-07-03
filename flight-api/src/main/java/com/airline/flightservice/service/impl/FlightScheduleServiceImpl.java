package com.airline.flightservice.service.impl;

import com.airline.flightservice.dto.FlightScheduleFilter;
import com.airline.flightservice.dto.FlightScheduleSeatInformationInputDto;
import com.airline.flightservice.kafka.event.NewFlightScheduleEvent;
import com.airline.flightservice.model.FlightInformation;
import com.airline.flightservice.model.FlightSchedule;
import com.airline.flightservice.repository.FlightRepository;
import com.airline.flightservice.repository.FlightScheduleRepository;
import com.airline.flightservice.service.FlightScheduleSeatInformationService;
import com.airline.flightservice.service.FlightScheduleService;
import com.airline.flightservice.specification.FlightScheduleSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class FlightScheduleServiceImpl implements FlightScheduleService {

    private final FlightScheduleRepository flightScheduleRepository;
    private final FlightScheduleSeatInformationService flightScheduleSeatInformationService;
    private final FlightRepository flightRepository;

    private final KafkaTemplate<String, NewFlightScheduleEvent> kafkaTemplate;

    public FlightScheduleServiceImpl(FlightScheduleRepository flightScheduleRepository, FlightScheduleSeatInformationService flightScheduleSeatInformationService,
                                     FlightRepository flightRepository, KafkaTemplate<String, NewFlightScheduleEvent> kafkaTemplate) {
        this.flightScheduleRepository = flightScheduleRepository;
        this.flightScheduleSeatInformationService = flightScheduleSeatInformationService;
        this.flightRepository = flightRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public List<FlightSchedule> getAllFlightSchedules() {

        return flightScheduleRepository.findAll();
    }

    @Override
    public FlightSchedule findById(Long id) {
        Optional<FlightSchedule> flightSchedule =flightScheduleRepository.findById(id);
        if (!flightSchedule.isPresent()) {
            throw new NoSuchElementException("No flight scheduls found by id: " + id);
        }
        return flightSchedule.get();
    }
    @Override
    public FlightSchedule addFlightSchedule(FlightSchedule flightSchedule) {
        FlightInformation fi = flightRepository.findById(flightSchedule.getFlightInformation().getId())
                .orElseThrow(() -> new EntityNotFoundException("FlightInformation not found"));


        flightSchedule.setFlightInformation(fi);

        FlightSchedule savedFlightSchedule = flightScheduleRepository.saveAndFlush(flightSchedule);
        FlightScheduleSeatInformationInputDto scheduleSeatInformationInputDto=
                FlightScheduleSeatInformationInputDto.builder()
                        .flightScheduleId(savedFlightSchedule.getId())
                        .bookingStatus(false)
                        .seatType("Economy")
                        .build();
        flightScheduleSeatInformationService.addSeatInformation(scheduleSeatInformationInputDto);

        NewFlightScheduleEvent event = new NewFlightScheduleEvent(
                flightSchedule.getId(),
                flightSchedule.getStartAirport(),
                flightSchedule.getEndAirport(),
                flightSchedule.getDepartureTime().toString(),
                flightSchedule.getArrivalTime().toString()
        );
        kafkaTemplate.send("new-flight-schedule-topic", event);
        return savedFlightSchedule;
    }

    @Override
    public List<FlightSchedule> searchSchedules(FlightScheduleFilter filter) {
        Specification<FlightSchedule> spec = FlightScheduleSpecification.build(filter);
        return flightScheduleRepository.findAll(spec);
    }

}
