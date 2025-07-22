package com.airline.pricingservice.exception;


public class InvalidAuthException extends RuntimeException{

    public InvalidAuthException(String message){
        super(message);
    }
}
