package com.airline.reservationservice.service.impl;

import com.airline.reservationservice.common.ConvertToJson;
import com.airline.reservationservice.common.MailType;
import com.airline.reservationservice.dto.ReservationDto;
import com.airline.reservationservice.dto.UserResponse;
import com.airline.reservationservice.mapper.ReservationMapper;
import com.airline.reservationservice.model.Reservation;
import com.airline.reservationservice.repository.ReservationRepository;
import com.airline.reservationservice.service.RemoteEmailSenderService;
import com.airline.reservationservice.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class ReservationServiceImpl implements ReservationService
{
    private final ReservationRepository reservationRepository ;
    private final RemoteEmailSenderService remoteEmailSenderService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${auth-service.url}") // e.g., http://auth-service:8000
    private String authServiceUrl;

    public ReservationServiceImpl(ReservationRepository reservationRepository, RemoteEmailSenderService remoteEmailSenderService) {
        this.reservationRepository = reservationRepository;
        this.remoteEmailSenderService = remoteEmailSenderService;
    }

    @Override
    public ReservationDto createReservation(ReservationDto reservationDTO, String authHeader) {

        log.info("CREATE RESERVATION");
        log.info("REQUEST { reservation: {} }", ConvertToJson.convertObjectToJsonString(reservationDTO));
      // na osnovu tokena iz header a izvlace se info o user u koji se prosledjuju ka
        // notification servisu za slanje mejla za potvrdu 
        try {
            Reservation reservation = ReservationMapper.mapToEntity(reservationDTO);
            reservation.setConfirmed(false);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authHeader);
            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<UserResponse> response = restTemplate.exchange(
                    authServiceUrl + "/" + reservationDTO.getUserId(),
                    HttpMethod.GET,
                    entity,
                    UserResponse.class
            );
            if (response==null) {
                throw new IllegalStateException("Failed to fetch user data from auth service");
            }
            log.info("Successfully fetched user data");
            log.info("User {}", ConvertToJson.convertObjectToJsonString(response.getBody()));

            Reservation savedItem = this.reservationRepository.save(reservation);
            log.info("Successfully created reservation");

            if (response.getStatusCode().is2xxSuccessful()) {
                UserResponse user = response.getBody();
                remoteEmailSenderService.sendConfirmationEmail(
                        user.getEmail(),
                        user.getToken(),
                        MailType.RESERVATION_CONFIRMATION
                );
                return ReservationMapper.mapToDto(savedItem);
            } else {
                throw new IllegalStateException("Failed to send e-mail.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while creating reservation", e);
        }

    }

    @Override
    public List<ReservationDto> getReservations() {
        try {
            List<Reservation> reservations = this.reservationRepository.findAll();
            return ReservationMapper.mapToDtoList(reservations);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch reservations", e);
        }
    }

    @Override
    public ReservationDto getReservationById(Integer id) {
        try {
            Reservation reservation = this.reservationRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Reservation not found with ID: " + id));

            return ReservationDto.builder()
                    .userId(reservation.getUserId())
                    .flightScheduleId(reservation.getFlightScheduleId())
                    .seatNumber(reservation.getSeatNumber())
                    .reservedAt(reservation.getReservedAt())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Error retrieving reservation with ID: " + id, e);
        }
    }


    @Override
    public void deleteReservation(Integer id) {
        try {
            if (!reservationRepository.existsById(id)) {
                throw new NoSuchElementException("Reservation with ID " + id + " does not exist");
            }
            this.reservationRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting reservation with ID: " + id, e);
        }
    }


    @Override
    public ReservationDto updateReservation(Integer id, ReservationDto reservationDTO) {
        try {
            Reservation reservation = this.reservationRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Reservation not found with ID: " + id));

            reservation.setUserId(reservationDTO.getUserId());
            reservation.setFlightScheduleId(reservationDTO.getFlightScheduleId());
            reservation.setSeatNumber(reservationDTO.getSeatNumber());

            Reservation savedItem = this.reservationRepository.save(reservation);

            return ReservationDto.builder()
                    .userId(savedItem.getUserId())
                    .flightScheduleId(savedItem.getFlightScheduleId())
                    .seatNumber(savedItem.getSeatNumber())
                    .reservedAt(savedItem.getReservedAt())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Failed to update reservation with ID: " + id, e);
        }
    }

}