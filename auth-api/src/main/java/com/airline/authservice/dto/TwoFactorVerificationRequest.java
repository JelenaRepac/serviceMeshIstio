package com.airline.authservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TwoFactorVerificationRequest {
    private String email;
    private String totpCode; // the code user reads from Google Authenticator

}
