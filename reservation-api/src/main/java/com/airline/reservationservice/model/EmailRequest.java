package com.airline.reservationservice.model;

import com.airline.reservationservice.common.MailType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailRequest {
    private String recipientEmail;
    private String token;
    private MailType mailType;
}
