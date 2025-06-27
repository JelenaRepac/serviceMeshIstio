package com.airline.notificationservice.service.impl;

import com.airline.notificationservice.common.MailType;
import com.airline.notificationservice.service.EmailSenderService;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailSenderServiceImpl implements EmailSenderService {

    //MAILJET
    //SLANJE MEJLA ZA POTVRDU NALOGA
    @Override
    public void sendConfirmationEmail(String recipientEmail, String token, MailType type)
            throws MailjetException, MailjetSocketTimeoutException {

        log.info("SEND confirmation email of type: {}", type);

        String apiKey = "2cc95c1ec4e397076d83befed980cbfd";
        String secretKey = "b559bd598c331f7f56eab8cf0bcf8f77";
        MailjetClient client = new MailjetClient(apiKey, secretKey, new ClientOptions("v3.1"));

        String subject;
        String htmlPart;

        switch (type) {
            case ACCOUNT_CONFIRMATION:
                subject = "Registration successfully completed!";
                htmlPart = "Your account has been successfully verified and activated.<br><br>" +
                        "You can now log in and start using the application.<br><br>" +
                        "Thank you for registering!";
                break;
            case RESERVATION_CONFIRMATION:
                subject = "Reservation Confirmation";
                htmlPart = "Thank you for your reservation! View details here: "
                        + "<a href='http://localhost:8001/reservation/confirm?token=" + token + "'>View Reservation</a>";
                break;
            default:
                throw new IllegalArgumentException("Unsupported email type");
        }

        JSONObject message = new JSONObject();
        message.put(Emailv31.Message.FROM, new JSONObject()
                .put("Email", "repac01jelena@gmail.com")
                .put("Name", "Airline"));
        message.put(Emailv31.Message.TO, new JSONArray()
                .put(new JSONObject()
                        .put("Email", recipientEmail)
                        .put("Name", "User")));
        message.put(Emailv31.Message.SUBJECT, subject);
        message.put(Emailv31.Message.HTMLPART, htmlPart);
        message.put(Emailv31.Message.CUSTOMID, "AppNotification");

        JSONObject requestBody = new JSONObject();
        requestBody.put(Emailv31.MESSAGES, new JSONArray().put(message));

        MailjetRequest request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, requestBody.getJSONArray(Emailv31.MESSAGES));

        MailjetResponse response = client.post(request);

        log.info("Confirmation mail sent");

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed to send email: " + response.getStatus() + " - " + response.getData());
        }
    }

}
