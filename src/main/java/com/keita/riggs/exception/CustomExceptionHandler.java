package com.keita.riggs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<Object> userNotFound(NotFoundException e) {
        HttpStatus badRequest = HttpStatus.NOT_FOUND;
        String simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm aa").format(new Date());
        ErrorResponse response = new ErrorResponse (
                badRequest.value(),
                e.getMessage(),
                simpleDateFormat,
                badRequest
        );
        return new ResponseEntity<>(response, badRequest);
    }
    @ExceptionHandler(value = {AlreadyExistsException.class})
    public ResponseEntity<Object> userAlreadyExist(AlreadyExistsException e) {
        HttpStatus badRequest = HttpStatus.OK;
        String simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm aa").format(new Date());
        ErrorResponse response = new ErrorResponse (
                badRequest.value(),
                e.getMessage(),
                simpleDateFormat,
                badRequest
        );
        return new ResponseEntity<>(response, badRequest);
    }


}
