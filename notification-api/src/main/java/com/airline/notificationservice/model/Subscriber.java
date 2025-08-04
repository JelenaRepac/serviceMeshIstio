package com.airline.notificationservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Subscriber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", length = 200)
    private String email;

    @Column(name = "subscription_date")
    private Date subscriptionDate;

    @Column(name = "status", length = 100)
    private String status;

    @Column(name = "name", length = 50)
    private String name;

}