package com.airline.authservice.exception;

public class ForbiddenExceptionCustom extends RuntimeException{
    String code;
    public ForbiddenExceptionCustom(String message, String code) {
        super(message);
        this.code=code;
    }
}
