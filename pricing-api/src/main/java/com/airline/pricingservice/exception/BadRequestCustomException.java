package com.airline.pricingservice.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadRequestCustomException extends RuntimeException{

    private String code;
    public BadRequestCustomException(String message, String code) {
        super(message);
        this.code=code;
    }
}
