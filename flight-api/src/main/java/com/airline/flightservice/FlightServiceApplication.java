package com.airline.flightservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@EntityScan(basePackages = {
		"com.airline.flightservice.model",
		"com.airlines.airlinesharedmodule"
})
public class FlightServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlightServiceApplication.class, args);
	}

}
