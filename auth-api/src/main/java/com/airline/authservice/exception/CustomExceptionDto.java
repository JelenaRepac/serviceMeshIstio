package com.airline.authservice.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CustomExceptionDto {

    private String code;
    private String message;
}
