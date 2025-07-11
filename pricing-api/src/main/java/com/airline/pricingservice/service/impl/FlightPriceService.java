package com.airline.pricingservice.service.impl;

import com.airlines.airlinesharedmodule.FlightPrice;

import java.util.List;

public interface FlightPriceService {

     List<FlightPrice> getPricesByFlightScheduleId(Long flightScheduleId);

     FlightPrice savePrice(FlightPrice price) ;

     void deletePrice(Long id) ;
}
