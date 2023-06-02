package com.keita.riggs.handler;

import com.keita.riggs.model.Authenticate;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CustomCredentialInputCheck extends RuntimeException{

    public CustomCredentialInputCheck(Authenticate authenticate, HttpStatus status, HttpServletResponse response) {

        super("Username and password is required");
        Map<String, Object> message = new HashMap<>();

        message.put("status", status.name());
        message.put("code", String.valueOf(status.value()));
        if (authenticate.getEmail().isEmpty()) {
            message.put("email", "Enter a valid username");
        }
        if (authenticate.getPassword().isEmpty()) {
            message.put("password", "Enter a valid password");
        }
        response.setContentType(APPLICATION_JSON_VALUE);
        try {
            new MessageMapper(response.getOutputStream(), message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CustomCredentialInputCheck(String errorMessage, HttpStatus status, HttpServletResponse response) {

        super("Invalid username and password entered");
        Map<String, Object> message = new HashMap<>();
        message.put("status", status.name());
        message.put("code", String.valueOf(status.value()));
        message.put("message", errorMessage);
        response.setContentType(APPLICATION_JSON_VALUE);
        try {
            new MessageMapper(response.getOutputStream(), message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
