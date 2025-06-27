package com.airline.reservationservice.kafka;

import com.airline.reservationservice.common.MailType;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailEvent implements Serializable {

    private String email;
    private String token;
    private MailType mailType;
}
