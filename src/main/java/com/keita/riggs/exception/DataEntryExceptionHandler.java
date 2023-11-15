package com.keita.riggs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class DataEntryExceptionHandler {

    @ExceptionHandler(value = {UnprocessableDataException.class})
    public ResponseEntity<Object> userRegistrationNotProcess(UnprocessableDataException e) throws NoSuchFieldException {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm aa").format(new Date());

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> errorMap = new HashMap<>();
        Map<String, String> addressError = new HashMap<>();
        Map<String, String> authError = new HashMap<>();

        map.put("status", status.name());
        map.put("code", String.valueOf(status.value()));
        boolean isAddressValid = true;
        boolean isAuthValid = true;
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            if (error.getField().contains("address")) {
                String replace = error.getField().trim().replace("address.", "");
                addressError.put(replace, error.getDefaultMessage());
                isAddressValid = false;
            }
            else if (error.getField().contains("auth")) {
                System.out.println("Auth " + error.getField());
                String replace = error.getField().trim().replace("auth.", "");
                authError.put(replace, error.getDefaultMessage());
                isAuthValid = false;
            }
            else {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
        }
        if (!isAddressValid) {
            errorMap.put("address", addressError);
        }
        if (!isAuthValid) {
            errorMap.put("auth", authError);
        }
        map.put("error", errorMap);

        ErrorResponse response = new ErrorResponse (
                status.value(),
                e.getMessage(),
                simpleDateFormat,
                status,
                map
        );
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(value = {UnprocessableRoomDataException.class})
    public ResponseEntity<Object> roomRegistrationNotProcess(UnprocessableRoomDataException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm aa").format(new Date());

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> errorMap = new HashMap<>();
        Map<String, String> roomError = new HashMap<>();

        map.put("status", status.name());
        map.put("code", String.valueOf(status.value()));
        int count = 0;
        int index = 0;
        boolean roomErrors = false;
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            String replace;
            System.out.println(error.getField() + ": " + error.getDefaultMessage());
            if (error.getField().contains("detail")) {
                replace = error.getField().trim().replace("detail.", "");
                roomError.put(replace, error.getDefaultMessage());
                count += 1;
                roomErrors = true;
            } else {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            if (count == 7) {
                count = 0;
                index++;
            }
        }
        if (roomErrors) {
            errorMap.put("detail", roomError);
        }
        map.put("error", errorMap);

        ErrorResponse response = new ErrorResponse (
                status.value(),
                e.getMessage(),
                simpleDateFormat,
                status,
                map
        );
        return new ResponseEntity<>(response, status);
    }

}
