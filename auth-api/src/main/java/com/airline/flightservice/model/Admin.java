package com.airline.flightservice.model;

import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

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