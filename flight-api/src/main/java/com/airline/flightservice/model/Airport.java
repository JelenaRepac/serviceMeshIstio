package com.airline.flightservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "airport", schema = "flights")
public class Airport {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "ident", length = 5)
    private String ident;

    @Column(name = "type", length = 100)
    private String type;

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "continent", length = 3)
    private String continent;

    @Column(name = "city", length = 200)
    private String city;
    @Column(name = "country", length = 3)
    private String country;

}