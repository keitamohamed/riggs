package com.keita.riggs.handler;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

public class InvalidInput extends RuntimeException{
    public InvalidInput(BindingResult result, HttpServletResponse response, String message) {
        super(message);
        MessageHandler.message(result, response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public static ResponseEntity<Object> roomError (BindingResult bindingResult, HttpStatus status) {
        Map<String, Object> message = new HashMap<>();
        Map<String, Object> errorMap = new HashMap<>();
        Map<String, String> roomError = new HashMap<>();

        message.put("status", status.name());
        message.put("code", String.valueOf(status.value()));
        int count = 0;
        int index = 0;
        boolean roomErrors = false;
        for (FieldError error : bindingResult.getFieldErrors()) {
            if (error.getField().contains("room")) {
                String replace = error.getField().trim().replace("room["+ index + "].", "");
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
            errorMap.put("room", roomError);
        }
        message.put("error", errorMap);
        return new ResponseEntity<>(message, status);
    }

    public static ResponseEntity<Object> userError (BindingResult bindingResult, HttpStatus status) {
        Map<String, Object> message = new HashMap<>();
        Map<String, Object> errorMap = new HashMap<>();
        Map<String, String> addressError = new HashMap<>();
        Map<String, String> authError = new HashMap<>();

        message.put("status", status.name());
        message.put("code", String.valueOf(status.value()));
        int index = 0;
        boolean isAddressValid = true;
        boolean isAuthValid = true;
        for (FieldError error : bindingResult.getFieldErrors()) {
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
        message.put("error", errorMap);
        return new ResponseEntity<>(message, status);
    }
}
