package com.airline.reservationservice.controller;

import com.airline.reservationservice.dto.ReservationDto;
import com.airline.reservationservice.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationDto>> getAllReservations() {
        List<ReservationDto> reservations = reservationService.getReservations();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(reservations);
    }

    @PostMapping
    public ResponseEntity<ReservationDto> createReservation(
            @RequestBody ReservationDto reservationDTO,
            @RequestHeader("Authorization") String authHeader) {
        ReservationDto createdReservation = reservationService.createReservation(reservationDTO, authHeader);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdReservation);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> getReservation(@PathVariable Integer id) {
        ReservationDto reservation = reservationService.getReservationById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(reservation);
    }

    @GetMapping("/user")
    public ResponseEntity<List<ReservationDto>> getReservationByUserId(@RequestParam(value = "userId") Long userId, @RequestHeader("Authorization") String authHeader) {
        List<ReservationDto> reservation = reservationService.getReservationByUserId(userId,authHeader);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(reservation);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ReservationDto> updateReservation(
            @PathVariable Integer id,
            @RequestBody ReservationDto reservationDTO) {
        ReservationDto updatedReservation = reservationService.updateReservation(id, reservationDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedReservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable Integer id) {
        reservationService.deleteReservation(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}

