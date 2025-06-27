package com.airline.authservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenAuthorizationDto {
    private String accessToken;
    private String refreshToken;

    private String qrCodeBase64;

}
