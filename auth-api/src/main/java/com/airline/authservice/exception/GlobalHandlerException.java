package com.airline.authservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalHandlerException {

    private static final String BAD_REQUEST_ERROR_CODE = "400";
    private static final String FORBIDDEN_ERROR_CODE   = "403";
    private static final String INTERVAL_SERVER_CODE   = "500";

    private static final String UNAUTHORIZED_ERROR_CODE = "401";
    private static final String UNAUTHORIZED_ERROR_CODE_GENERAL = "401.1";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomExceptionDto handleMethodArgumentNotValid(MethodArgumentNotValidException mne) {
        Map<String, String> validationMessages = extractValidationMessages(mne);
        return CustomExceptionDto.builder().code(BAD_REQUEST_ERROR_CODE)
                .message("Request arguments: " + validationMessages.toString())
                .build();
    }
    @ExceptionHandler(InvalidAuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CustomExceptionDto handleInvalidAuth(InvalidAuthException mne) {
        return CustomExceptionDto.builder().code(UNAUTHORIZED_ERROR_CODE_GENERAL)
                .message(mne.getMessage())
                .build();
    }

    @ExceptionHandler(ForbiddenExceptionCustom.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public CustomExceptionDto handleForbidden(ForbiddenExceptionCustom mne) {
        return CustomExceptionDto.builder().code(FORBIDDEN_ERROR_CODE)
                .message(mne.getMessage())
                .build();
    }



    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CustomExceptionDto handleOthers(Exception ex) {
        return CustomExceptionDto.builder().code(INTERVAL_SERVER_CODE)
                .message(ex.getMessage())
                .build();
    }

    private Map<String, String> extractValidationMessages(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        Map<String, String> validationMessages = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            validationMessages.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return validationMessages;
    }

}
