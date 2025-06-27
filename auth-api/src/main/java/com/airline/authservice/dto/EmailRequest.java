package com.airline.authservice.dto;

import com.airline.authservice.common.MailType;
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
