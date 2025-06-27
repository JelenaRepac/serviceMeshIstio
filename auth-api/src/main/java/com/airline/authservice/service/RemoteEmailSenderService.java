package com.airline.authservice.service;

import com.airline.authservice.common.MailType;

public interface RemoteEmailSenderService {

     void sendConfirmationEmail(String email, String token, MailType mailType);
}
