package com.airline.flightservice.exception;

import com.airline.flightservice.config.LocaleLanguage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    private final MessageSource messageSource;
    private final LocaleLanguage localeLanguage;

    public GlobalExceptionHandler(MessageSource messageSource, LocaleLanguage localeLanguage) {
        this.messageSource = messageSource;
        this.localeLanguage = localeLanguage;
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public  CustomExceptionDto handleEmptyDataAccessException(NotFoundException ex){
        log.error(" Response: {}", ex.getMessage());
        return CustomExceptionDto.builder().
                errorCode(ex.getCode()).
                errorMessage(ex.getMessage()).
                build();
    }

    @ExceptionHandler(BadRequestCustomException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public  CustomExceptionDto handleEmptyDataAccessException(BadRequestCustomException ex){
        log.error(" Response: {}", ex.getMessage());
        return CustomExceptionDto.builder().
                errorCode(ex.getCode()).
                errorMessage(ex.getMessage()).
                build();
    }
    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public  CustomExceptionDto handleSqlException(SQLException ex){
        log.error(" Response: {}", ex.getMessage());
        return CustomExceptionDto.builder().
                errorCode(messageSource.getMessage("error.code.internal_server_error", null, localeLanguage.getLocale())).
                errorMessage(ex.getMessage()).
                build();
    }

    @ExceptionHandler(InvalidAuthException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public  CustomExceptionDto handleAccessDeniedException(InvalidAuthException ex){
        log.error(" Response: {}", ex.getMessage());
        return CustomExceptionDto.builder().
                errorCode(messageSource.getMessage("error.code.bad_request", null, localeLanguage.getLocale())).
                errorMessage(ex.getMessage()).
                build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomExceptionDto handelBadParametersFormat(MethodArgumentNotValidException ex){
        log.error(" Response: { " + ex.getMessage() + " }");
        return CustomExceptionDto.builder().
                errorCode(messageSource.getMessage("error.code.bad_request", null, localeLanguage.getLocale())).
                errorMessage(ex.getMessage()).
                build();
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public  CustomExceptionDto handleOthers(Exception ex){
        log.error(" Response: {}", ex.getMessage()+ex.getCause()+ex.getStackTrace()+ex.getClass());
        return CustomExceptionDto.builder().
                errorCode(messageSource.getMessage("error.code.internal_server_error", null, localeLanguage.getLocale())).
                errorMessage(ex.getMessage()+ ex.getClass()).
                build();
    }


}