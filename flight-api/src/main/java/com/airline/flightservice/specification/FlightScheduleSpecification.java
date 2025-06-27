package com.airline.flightservice.specification;

import com.airline.flightservice.dto.FlightScheduleFilter;
import com.airline.flightservice.model.FlightSchedule;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FlightScheduleSpecification {

    public static Specification<FlightSchedule> build(FlightScheduleFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getFrom() != null) {
                predicates.add(cb.like(root.get("startAirport"), filter.getFrom()+"%"));
            }
            if (filter.getTo() != null) {
                predicates.add(cb.like(root.get("endAirport"), filter.getTo()+"%"));
            }
            if (filter.getDepartureDate() != null) {
                java.sql.Date sqlDate = java.sql.Date.valueOf(filter.getDepartureDate());
                predicates.add(cb.equal(root.get("departureDate"), sqlDate));
            }
            if (filter.getArrivalDate() != null) {
                java.sql.Date sqlDate = java.sql.Date.valueOf(filter.getArrivalDate());
                predicates.add(cb.equal(root.get("arrivalDate"), sqlDate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
