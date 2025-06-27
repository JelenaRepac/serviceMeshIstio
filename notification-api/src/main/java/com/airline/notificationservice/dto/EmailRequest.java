package com.airline.notificationservice.dto;

import com.airline.notificationservice.common.MailType;
import lombok.Data;

@Data
public class EmailRequest {
    private String recipientEmail;
    private String token;
    private MailType mailType;
}
