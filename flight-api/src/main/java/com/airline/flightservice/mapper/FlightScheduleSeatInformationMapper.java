package com.airline.flightservice.mapper;

import com.airline.flightservice.dto.FlightScheduleSeatInformationInputDto;
import com.airline.flightservice.dto.FlightScheduleSeatInformationOutputDto;
import com.airline.flightservice.model.FlightSchedule;
import com.airline.flightservice.model.FlightScheduleSeatInformation;

import java.util.List;
import java.util.stream.Collectors;

public class FlightScheduleSeatInformationMapper {

    public static FlightScheduleSeatInformation mapToFlightScheduleSeatInformation(FlightScheduleSeatInformationInputDto inputDto, FlightSchedule flightSchedule) {
        FlightScheduleSeatInformation seatInformation = new FlightScheduleSeatInformation();
        seatInformation.setId(inputDto.getId());
        seatInformation.setSeatType(inputDto.getSeatType());
        seatInformation.setBookingStatus(inputDto.getBookingStatus());
        seatInformation.setFlightSchedule(flightSchedule);
        return seatInformation;
    }

    public static FlightScheduleSeatInformationOutputDto mapToFlightScheduleSeatInformationOutputDto(FlightScheduleSeatInformation seatInformation) {
        return new FlightScheduleSeatInformationOutputDto(
                seatInformation.getId(),
                seatInformation.getSeatType(),
                seatInformation.getBookingStatus(),
                seatInformation.getSeatNumber(),
                seatInformation.getFlightSchedule()
        );
    }

    public static List<FlightScheduleSeatInformationOutputDto> mapToFlightScheduleSeatInformationOutputDtoList(List<FlightScheduleSeatInformation> seatInformationList) {
        return seatInformationList.stream()
                .map(FlightScheduleSeatInformationMapper::mapToFlightScheduleSeatInformationOutputDto)
                .collect(Collectors.toList());
    }
}
