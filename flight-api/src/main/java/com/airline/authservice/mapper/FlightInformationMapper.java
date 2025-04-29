package com.airline.authservice.mapper;

import com.airline.authservice.dto.FlightInformationDto;
import com.airline.authservice.model.FlightInformation;

import java.util.List;
import java.util.stream.Collectors;

public class FlightInformationMapper {

    public static FlightInformation mapToFlightInformation(FlightInformationDto flightInformationDto) {
        FlightInformation flightInformation = new FlightInformation(
                flightInformationDto.getId(),
                flightInformationDto.getFlightName(),
                flightInformationDto.getCapacity(),
                flightInformationDto.getFlightType(),
                flightInformationDto.getSeatType(),
                flightInformationDto.getMaximumWeightForPassenger(),
                flightInformationDto.getAirlineService()
        );
        return flightInformation;
    }

    public static FlightInformationDto mapToFlightInformationDto(FlightInformation flightInformation) {
        FlightInformationDto flightInformationDto = new FlightInformationDto(
                flightInformation.getId(),
                flightInformation.getFlightName(),
                flightInformation.getCapacity(),
                flightInformation.getFlightType(),
                flightInformation.getSeatType(),
                flightInformation.getMaximumWeightForPassenger(),
                flightInformation.getAirlineService()
        );
        return flightInformationDto;
    }

    public static List<FlightInformationDto> mapToFlightInformationListDto(List<FlightInformation> flightInformationList) {
        return flightInformationList.stream()
                .map(FlightInformationMapper::mapToFlightInformationDto)
                .collect(Collectors.toList());
    }

}