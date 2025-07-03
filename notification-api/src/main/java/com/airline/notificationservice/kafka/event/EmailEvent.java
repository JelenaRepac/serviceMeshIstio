package com.airline.notificationservice.kafka.event;

import com.airline.notificationservice.common.MailType;
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
