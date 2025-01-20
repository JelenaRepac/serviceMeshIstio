package com.airline.flightservice.model;


import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@Data
@Entity
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "AIRPLANE", referencedColumnName = "ID",nullable = false)
    private Airplane airplane;

    @Column(nullable = false)
    private String origin;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private int miles;

    @Column(nullable = false)
    private int price;

    @Column(name="flightSeats")
    private int availableSeats;

    @Column(name = "flightCanceled",nullable = false)
    private boolean flightCanceled;

    public Flight(){
        flightCanceled = false;
    }


}