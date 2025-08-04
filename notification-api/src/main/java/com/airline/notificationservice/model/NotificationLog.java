package com.airline.notificationservice.model;


import jakarta.persistence.Id;
import lombok.*;
//import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@Document(collection = "notification_log")
public class NotificationLog {

    @Id
    private String id;

    private String recipientEmail;
    private String subject;
    private String messageBody;
    private LocalDateTime sentTime;
    private String status; // SENT, FAILED, RETRY

}