package com.airline.authservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class InvalidAuthException extends RuntimeException{

    public InvalidAuthException(String message){
        super(message);
    }
}
