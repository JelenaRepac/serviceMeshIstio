package com.airline.reservationservice.mapper;

import com.airline.reservationservice.dto.ReservationDto;
import com.airline.reservationservice.model.Reservation;

import java.util.List;
import java.util.stream.Collectors;

public class ReservationMapper {

    public static Reservation mapToEntity(ReservationDto dto) {
        Reservation reservation = new Reservation();
        reservation.setId(dto.getId());
        reservation.setSeatNumber(dto.getSeatNumber());
        reservation.setUserId(dto.getUserId());
        reservation.setFlightScheduleId(dto.getFlightScheduleId());
        reservation.setConfirmed(dto.getConfirmed());
        reservation.setReservedAt(dto.getReservedAt());
        return reservation;
    }

    public static ReservationDto mapToDto(Reservation entity) {
        ReservationDto dto = new ReservationDto();
        dto.setId(entity.getId());
        dto.setSeatNumber(entity.getSeatNumber());
        dto.setUserId(entity.getUserId());
        dto.setFlightScheduleId(entity.getFlightScheduleId());
        dto.setConfirmed(entity.getConfirmed());
        dto.setReservedAt(entity.getReservedAt());
        return dto;
    }

    public static List<ReservationDto> mapToDtoList(List<Reservation> entities) {
        return entities.stream()
                .map(ReservationMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public static List<Reservation> mapToEntityList(List<ReservationDto> dtos) {
        return dtos.stream()
                .map(ReservationMapper::mapToEntity)
                .collect(Collectors.toList());
    }
}
