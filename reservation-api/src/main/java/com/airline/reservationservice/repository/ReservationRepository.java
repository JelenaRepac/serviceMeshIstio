package com.airline.reservationservice.repository;

import com.airlines.airlinesharedmodule.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    List<Reservation> findByUserId(Long userId);
}