package com.airline.reservationservice.service;

import com.airline.reservationservice.common.MailType;

public interface RemoteEmailSenderService {

     void sendConfirmationEmail(String email, String token, MailType mailType);
}
