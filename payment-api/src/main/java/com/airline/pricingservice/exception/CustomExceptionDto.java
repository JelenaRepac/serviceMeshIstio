package com.airline.pricingservice.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CustomExceptionDto {

    private String errorCode;
    private String errorMessage;
}
