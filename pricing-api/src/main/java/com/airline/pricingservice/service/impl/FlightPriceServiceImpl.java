package com.airline.pricingservice.service.impl;

import com.airline.pricingservice.repository.FlightPriceRepository;
import com.airlines.airlinesharedmodule.FlightPrice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FlightPriceServiceImpl implements FlightPriceService {

    private final FlightPriceRepository flightPriceRepository;

    public FlightPriceServiceImpl(FlightPriceRepository flightPriceRepository) {
        this.flightPriceRepository = flightPriceRepository;
    }

    @Override
    public List<FlightPrice> getPricesByFlightScheduleId(Long flightScheduleId) {
        return flightPriceRepository.findByFlightScheduleId(flightScheduleId);
    }

    @Override
    public FlightPrice savePrice(FlightPrice price) {

        return flightPriceRepository.save(price);
    }

    @Override
    public void deletePrice(Long id) {
        flightPriceRepository.deleteById(id);
    }
}
