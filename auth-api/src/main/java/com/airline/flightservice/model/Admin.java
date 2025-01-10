package com.airline.flightservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "admin", schema = "airline_users")
public class Admin {

    @Id
    @Column(nullable = false,unique = true)
    private String username;

    @Column(nullable = false)
    private String password;
}