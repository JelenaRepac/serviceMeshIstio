package com.airline.pricingservice.service;

import com.airline.pricingservice.common.MailType;

public interface RemoteEmailSenderService {

    void sendEmail(String email, String token, MailType mailType);


}
