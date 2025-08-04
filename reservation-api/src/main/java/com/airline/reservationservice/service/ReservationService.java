package com.airline.reservationservice.service;

import com.airline.reservationservice.dto.ReservationDto;

import java.util.List;

public interface ReservationService {


    ReservationDto createReservation(ReservationDto reservationDTO, String authHeader);

    List<ReservationDto> getReservations();
    ReservationDto getReservationById(Integer id);

    void deleteReservation(Integer id);

    ReservationDto updateReservation(Integer id, ReservationDto reservationDTO);

    List<ReservationDto> getReservationByUserId(Long userId, String authHeader);
}
