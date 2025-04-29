package com.airline.authservice.exception;


public class InvalidAuthException extends RuntimeException{

    public InvalidAuthException(String message){
        super(message);
    }
}
