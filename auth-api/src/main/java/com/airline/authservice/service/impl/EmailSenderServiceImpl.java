package com.airline.authservice.service.impl;

import com.airline.authservice.service.EmailSenderService;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    //MAILJET

    @Override
    public void sendConfirmationEmail(String recipientEmail, String token) throws MailjetException, MailjetSocketTimeoutException {
        String apiKey = "2cc95c1ec4e397076d83befed980cbfd";
        String secretKey = "b559bd598c331f7f56eab8cf0bcf8f77";
        MailjetClient client = new MailjetClient(apiKey, secretKey, new ClientOptions("v3.1"));

        JSONObject message = new JSONObject();
        message.put(Emailv31.Message.FROM, new JSONObject()
                .put("Email", "repac01jelena@gmail.com")
                .put("Name", "Airline"));
        message.put(Emailv31.Message.TO, new JSONArray()
                .put(new JSONObject()
                        .put("Email", recipientEmail)
                        .put("Name", "User")));
        message.put(Emailv31.Message.SUBJECT, "Complete Registration!");
        message.put(Emailv31.Message.HTMLPART, "To confirm your account, please click here: <a href='http://localhost:8000/auth/confirm-account?token=" + token + "'>Confirm Account</a>");
        message.put(Emailv31.Message.CUSTOMID, "AppRegistration");

        JSONObject requestBody = new JSONObject();
        requestBody.put(Emailv31.MESSAGES, new JSONArray().put(message));

        MailjetRequest request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, requestBody.getJSONArray(Emailv31.MESSAGES));

        MailjetResponse response = client.post(request);

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed to send email: " + response.getStatus() + " - " + response.getData());
        }
    }
}
