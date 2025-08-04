package com.airline.reservationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
		"com.airline.reservationservice",
		"com.airlines.airlinesharedmodule"
})
@EnableScheduling
@EntityScan(basePackages = {
		"com.airline.reservationservice.model",
		"com.airlines.airlinesharedmodule"  // <-- include this
})
public class ReservationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationServiceApplication.class, args);
	}

}
