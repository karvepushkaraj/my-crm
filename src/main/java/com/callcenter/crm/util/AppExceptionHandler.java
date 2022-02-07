package com.callcenter.crm.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler({ServiceLayerException.class, JsonProcessingException.class, RuntimeException.class})
    public ResponseEntity<ErrorMessage> handleException(ServiceLayerException e, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(new Date(System.currentTimeMillis()),
                HttpStatus.BAD_REQUEST, e.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), errorMessage.getStatus());
    }

}
