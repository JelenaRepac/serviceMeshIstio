package com.airline.flightservice.mapper;

import com.airline.flightservice.dto.FlightInformationDto;
import com.airline.flightservice.model.FlightInformation;

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
}