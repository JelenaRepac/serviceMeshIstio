package com.airline.flightservice.controller;

import com.airline.flightservice.dto.FlightScheduleSeatInformationInputDto;
import com.airline.flightservice.dto.FlightScheduleSeatInformationOutputDto;
import com.airline.flightservice.service.FlightScheduleSeatInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flightSchedule/seat")
public class FlightScheduleSeatInformationController {

    @Autowired
    private FlightScheduleSeatInformationService seatInformationService;

    @PostMapping
    public ResponseEntity<List<FlightScheduleSeatInformationOutputDto>> addSeatInformation(@RequestBody FlightScheduleSeatInformationInputDto inputDto) {
        List<FlightScheduleSeatInformationOutputDto> savedSeatInformation = seatInformationService.addSeatInformation(inputDto);
        return ResponseEntity.ok(savedSeatInformation);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<FlightScheduleSeatInformationOutputDto> getSeatInformationById(@PathVariable Long id) {
//        FlightScheduleSeatInformationOutputDto seatInformation = seatInformationService.getSeatInformationById(id);
//        return ResponseEntity.ok(seatInformation);
//    }



    @PutMapping("/{id}")
    public ResponseEntity<FlightScheduleSeatInformationOutputDto> updateSeatInformation(@PathVariable Long id, @RequestBody FlightScheduleSeatInformationInputDto inputDto) {
        FlightScheduleSeatInformationOutputDto updatedSeatInformation = seatInformationService.updateSeatInformation(id, inputDto);
        return ResponseEntity.ok(updatedSeatInformation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeatInformation(@PathVariable Long id) {
        seatInformationService.deleteSeatInformation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{flightScheduleId}")
    public ResponseEntity<List<FlightScheduleSeatInformationOutputDto>> getSeatInformationByFlightScheduleId(@PathVariable Long flightScheduleId,
                                                                                                             @RequestParam(required = false) Boolean bookingStatus,
                                                                                                             @RequestParam(required = false, value = "seatNumber") String seatNumber) {
        List<FlightScheduleSeatInformationOutputDto> seatInformationList = seatInformationService.getSeatInformationByFlightScheduleIdAndBookingStatusAndSeatNumber(flightScheduleId, bookingStatus, seatNumber);
        return ResponseEntity.ok(seatInformationList);
    }

    //    localhost:8083/api/seats/booking-status?bookingStatus=false


    //    localhost:8083/api/seats/seat-type?seatType=Economy
    @GetMapping("/seat-type")
    public ResponseEntity<List<FlightScheduleSeatInformationOutputDto>> getSeatInformationBySeatType(@RequestParam String seatType) {
        List<FlightScheduleSeatInformationOutputDto> seatInformationList = seatInformationService.getSeatInformationBySeatType(seatType);
        return ResponseEntity.ok(seatInformationList);
    }
}