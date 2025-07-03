package com.airline.flightservice.service.impl;

import com.airline.flightservice.dto.City;
import com.airline.flightservice.dto.FlightInformationDto;
import com.airline.flightservice.exception.NotFoundException;
import com.airline.flightservice.kafka.event.NewFlightScheduleEvent;
import com.airline.flightservice.model.FlightInformation;
import com.airline.flightservice.repository.FlightRepository;
import com.airline.flightservice.service.FlightService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.airline.flightservice.mapper.FlightInformationMapper.*;

@Service
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;


    public FlightServiceImpl(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }


    @Override
    public FlightInformationDto addFlight(FlightInformationDto flightInformationDto) {
        FlightInformation flight = mapToFlightInformation(flightInformationDto);
        FlightInformation savedFlight = flightRepository.save(flight);


        return mapToFlightInformationDto(savedFlight);
    }
    @Override
    public FlightInformationDto getFlightById(Long id) {
        Optional<FlightInformation> flightInformation= flightRepository.findById(id);
        if(!flightInformation.isPresent()){
            throw new NotFoundException("Flight with provided Id doesn't exist!", "404");
        }

        return mapToFlightInformationDto(flightInformation.get());
    }

    @Override
    public List<FlightInformationDto> getAllFlights() {
       List<FlightInformation> flightInformationDtoList = flightRepository.findAll();

       return mapToFlightInformationListDto(flightInformationDtoList);

    }

    @Override
    public void deleteFlight(Long id) {
        flightRepository.deleteById(id);
    }




}