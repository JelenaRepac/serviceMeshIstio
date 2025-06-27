package com.airline.flightservice.service.impl;

import com.airline.flightservice.dto.FlightScheduleSeatInformationInputDto;
import com.airline.flightservice.dto.FlightScheduleSeatInformationOutputDto;
import com.airline.flightservice.mapper.FlightScheduleSeatInformationMapper;
import com.airline.flightservice.model.FlightSchedule;
import com.airline.flightservice.model.FlightScheduleSeatInformation;
import com.airline.flightservice.repository.FlightScheduleRepository;
import com.airline.flightservice.repository.FlightScheduleSeatInformationRepository;
import com.airline.flightservice.service.FlightScheduleSeatInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightScheduleSeatInformationServiceImpl implements FlightScheduleSeatInformationService {

    @Autowired
    private FlightScheduleSeatInformationRepository seatInformationRepository;

    @Autowired
    private FlightScheduleRepository flightScheduleRepository;

    @Override
    public List<FlightScheduleSeatInformationOutputDto> addSeatInformation(FlightScheduleSeatInformationInputDto inputDto) {
        FlightSchedule flightSchedule = flightScheduleRepository.findById(inputDto.getFlightScheduleId())
                .orElseThrow(() -> new RuntimeException("Flight Schedule not found"));

        List<FlightScheduleSeatInformation> seatList = new ArrayList<>();

        // Example: Rows A to F, seats 1 to 6 (A1-A6, B1-B6, ..., F1-F6)
        for (char row = 'A'; row <= 'F'; row++) {
            for (int col = 1; col <= 6; col++) {
                String seatType = inputDto.getSeatType(); // e.g., "economy"
                Boolean bookingStatus = inputDto.getBookingStatus();
                String seatId = row + String.valueOf(col); // e.g., "A1"

                FlightScheduleSeatInformation seat = new FlightScheduleSeatInformation();
                seat.setSeatType(seatType + "-" + seatId); // e.g., "economy-A1"
                seat.setBookingStatus(bookingStatus);
                seat.setFlightSchedule(flightSchedule);

                seatList.add(seat);
            }
        }

        List<FlightScheduleSeatInformation> savedSeats = seatInformationRepository.saveAll(seatList);

        return FlightScheduleSeatInformationMapper.mapToFlightScheduleSeatInformationOutputDtoList(savedSeats);
    }

    @Override
    public FlightScheduleSeatInformationOutputDto getSeatInformationById(Long id) {
        FlightScheduleSeatInformation seatInformation = seatInformationRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Seat information does not exist!"));
        return FlightScheduleSeatInformationMapper.mapToFlightScheduleSeatInformationOutputDto(seatInformation);
    }

    @Override
    public List<FlightScheduleSeatInformationOutputDto> getAllSeatInformation() {
        return seatInformationRepository.findAll().stream()
                .map(FlightScheduleSeatInformationMapper::mapToFlightScheduleSeatInformationOutputDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FlightScheduleSeatInformationOutputDto> getAllSeatInformationByFlight(String flightId) {
        return seatInformationRepository.findAll().stream()
                .map(FlightScheduleSeatInformationMapper::mapToFlightScheduleSeatInformationOutputDto)
                .collect(Collectors.toList());
    }

    @Override
    public FlightScheduleSeatInformationOutputDto updateSeatInformation(Long id, FlightScheduleSeatInformationInputDto inputDto) {
        FlightScheduleSeatInformation seatInformation = seatInformationRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Seat information does not exist!"));

        FlightSchedule flightSchedule = flightScheduleRepository.findById(inputDto.getFlightScheduleId())
                .orElseThrow(() -> new RuntimeException("Flight Schedule not found"));

        seatInformation.setSeatType(inputDto.getSeatType());
        seatInformation.setBookingStatus(inputDto.getBookingStatus());
        seatInformation.setFlightSchedule(flightSchedule);

        FlightScheduleSeatInformation updatedSeatInformation = seatInformationRepository.save(seatInformation);
        return FlightScheduleSeatInformationMapper.mapToFlightScheduleSeatInformationOutputDto(updatedSeatInformation);
    }

    @Override
    public void deleteSeatInformation(Long id) {
        FlightScheduleSeatInformation seatInformation = seatInformationRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Seat information does not exist!"));
        seatInformationRepository.deleteById(id);
    }


    @Override
    public List<FlightScheduleSeatInformationOutputDto> getSeatInformationByFlightScheduleIdAndBookingStatus(Long flightScheduleId, Boolean bookingStatus) {

        List<FlightScheduleSeatInformationOutputDto> flightScheduleSeatInformationOutputDto = seatInformationRepository.findByFlightScheduleId(flightScheduleId).stream()
                .map(FlightScheduleSeatInformationMapper::mapToFlightScheduleSeatInformationOutputDto)
                .collect(Collectors.toList());

        if (bookingStatus != null) {
            flightScheduleSeatInformationOutputDto.removeIf(flightScheduleSeatInformation ->
                    !flightScheduleSeatInformation.getBookingStatus().equals(bookingStatus));
        }

        return flightScheduleSeatInformationOutputDto;
    }


    @Override
    public List<FlightScheduleSeatInformationOutputDto> getSeatInformationBySeatType(String seatType) {
        return seatInformationRepository.findBySeatType(seatType).stream()
                .map(FlightScheduleSeatInformationMapper::mapToFlightScheduleSeatInformationOutputDto)
                .collect(Collectors.toList());
    }
}
