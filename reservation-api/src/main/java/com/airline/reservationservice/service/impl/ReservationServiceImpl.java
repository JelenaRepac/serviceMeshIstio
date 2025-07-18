package com.airline.reservationservice.service.impl;

import com.airline.reservationservice.common.ConvertToJson;
import com.airline.reservationservice.common.MailType;
import com.airline.reservationservice.dto.FlightScheduleResponse;
import com.airline.reservationservice.dto.FlightScheduleSeatResponse;
import com.airline.reservationservice.dto.ReservationDto;
import com.airline.reservationservice.dto.UserResponse;
import com.airline.reservationservice.exception.BadRequestCustomException;
import com.airline.reservationservice.kafka.EmailEvent;
import com.airline.reservationservice.kafka.UpcomingFlightNotificationEvent;
import com.airline.reservationservice.mapper.ReservationMapper;
import com.airline.reservationservice.model.Reservation;
import com.airline.reservationservice.repository.ReservationRepository;
import com.airline.reservationservice.service.RemoteEmailSenderService;
import com.airline.reservationservice.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.h2.engine.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final RemoteEmailSenderService remoteEmailSenderService;

    private final WebClient webClient;
    private final KafkaTemplate<String, UpcomingFlightNotificationEvent> kafkaTemplate;


    @Value("${auth-service.url}")
    private String authServiceUrl;
    @Value("${flight-service.url}")
    private String flightScheduleUrl;

    // dodati generisanje tokena
    // kreirati user a job
    @Value("${job.token}")
    private String jobToken;


    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  RemoteEmailSenderService remoteEmailSenderService,
                                  WebClient.Builder webClientBuilder,
                                  KafkaTemplate<String, UpcomingFlightNotificationEvent> kafkaTemplate) {
        this.reservationRepository = reservationRepository;
        this.remoteEmailSenderService = remoteEmailSenderService;
        this.webClient = webClientBuilder.build();
        this.kafkaTemplate = kafkaTemplate;
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


            UserResponse response = getUserById(reservation, authHeader);
            if (response == null) {
                throw new IllegalStateException("Failed to fetch user data from auth service");
            }
            log.info("Successfully fetched user data");
            log.info("User {}", ConvertToJson.convertObjectToJsonString(response));

            //GET KONKRETNO MESTO - PROVERA DA LI JE REZERVISANO
            List<FlightScheduleSeatResponse> flightScheduleSeatResponseResponseEntity = getSeatInfoByFlightScheduleIdAndSeatNumber(reservation, authHeader);
            if (response != null) {
                FlightScheduleSeatResponse flightScheduleSeatResponse = flightScheduleSeatResponseResponseEntity.get(0);
                if (flightScheduleSeatResponse.getBookingStatus().equals(true)) {
                    throw new BadRequestCustomException("Seat is reserved.", "400");
                } else {
                    //REZERVACIJA
                    flightScheduleSeatResponse.setFlightScheduleId(reservationDTO.getFlightScheduleId());
                    reserveSeat(flightScheduleSeatResponse, authHeader);
                }
            } else {
                throw new BadRequestCustomException("Failed to fetch seat " + reservation.getSeatNumber(), "400");
            }
            Reservation savedItem = this.reservationRepository.save(reservation);
            log.info("Successfully created reservation");

            //UPDATE FLIGHT SCHEDULE SEAT

            if (response != null) {
                UserResponse user = response;
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
            throw new RuntimeException("Error occurred while creating reservation" + e.getMessage());
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

    @Override
    public List<ReservationDto> getReservationByUserId(Long userId) {
        try {
            List<Reservation> reservations = this.reservationRepository.findByUserId(userId);

            if (reservations.isEmpty()) {
                throw new NoSuchElementException("No reservations found for user ID: " + userId);
            }
            return ReservationMapper.mapToDtoList(reservations);

        } catch (Exception e) {
            throw new RuntimeException("Error retrieving reservation for user id: " + userId, e);
        }
    }

    public FlightScheduleResponse getFlightSchedule(Reservation reservation, String authHeader) {
        return webClient.get()
                .uri(flightScheduleUrl + "/" + reservation.getFlightScheduleId())
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .retrieve()
                .bodyToMono(FlightScheduleResponse.class)
                .block();
    }

    public UserResponse getUserById(Reservation reservation, String authHeader) {
        return webClient.get()
                .uri(authServiceUrl + "/" + reservation.getUserId())
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .block();
    }

    public List<FlightScheduleSeatResponse> getSeatInfoByFlightScheduleIdAndSeatNumber(Reservation reservation, String authHeader) {
        String url = flightScheduleUrl + "/seat/" + reservation.getFlightScheduleId()
                + "?seatNumber=" + reservation.getSeatNumber();

        return webClient.get()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .retrieve()
                .bodyToFlux(FlightScheduleSeatResponse.class)
                .collectList()
                .block();
    }


    public FlightScheduleSeatResponse reserveSeat(FlightScheduleSeatResponse flightScheduleSeatResponse, String authHeader) {
        flightScheduleSeatResponse.setBookingStatus(true);

        String url = flightScheduleUrl + "/seat/" + flightScheduleSeatResponse.getId();

        return webClient.put()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(flightScheduleSeatResponse)
                .retrieve()
                .bodyToMono(FlightScheduleSeatResponse.class)
                .block();
    }


    @Scheduled(cron = "0 0 8 * * *", zone = "Europe/Belgrade")
    public void emitUpcomingFlightsNotifications() {
        LocalDate today = LocalDate.now();
        List<Long> targetDays = List.of(0L, 7L, 3L, 1L);

        List<Reservation> allReservations = reservationRepository.findAll();

        for (Reservation reservation : allReservations) {
            FlightScheduleResponse schedule = getFlightSchedule(reservation, jobToken);

            if (schedule == null) continue;

            LocalDate departureDate = schedule.getDepartureDate();

            for (Long daysBefore : targetDays) {
                if (departureDate.equals(today.plusDays(daysBefore))) {
                    UserResponse user = getUserById(reservation, jobToken);

                    if (user == null) continue;

                    remoteEmailSenderService.sendNotificationMail(
                            user.getEmail(),
                            schedule.getDepartureDate(),
                            schedule.getDepartureTime(),
                            daysBefore);

                    break;
                }
            }
        }

    }

}