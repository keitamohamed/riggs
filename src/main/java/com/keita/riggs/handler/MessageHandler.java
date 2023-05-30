package com.keita.riggs.handler;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Service
public class MessageHandler {

    static void message(BindingResult bindingResult, HttpServletResponse response, HttpStatus status) {

        try {
            Map<String, Object> message = setErrorMessage(bindingResult, response, status);
            new MessageMapper(response.getOutputStream(), message);
        }catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private static Map<String, Object> setErrorMessage(BindingResult bindingResult, HttpServletResponse response, HttpStatus status) {
        Map<String, Object> message = new HashMap<>();
        Map<String, String> errorMap = new HashMap<>();

        message.put("status", status.name());
        message.put("code", String.valueOf(status.value()));
        for (FieldError error : bindingResult.getFieldErrors()) {
            errorMap.put(error.getField(), error.getDefaultMessage());
        }
        message.put("error", errorMap);
        response.setStatus(status.value());
        response.setContentType(APPLICATION_JSON_VALUE);
        return message;
    }

    static void message(HttpServletResponse response, String message) {
        try {
            Map<String, String> map = new HashMap<>();

            map.put("status", String.valueOf(HttpServletResponse.SC_OK));
            map.put("code", String.valueOf(HttpStatus.OK.value()));
            map.put("message", message);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(APPLICATION_JSON_VALUE);

            new MessageMapper(response.getOutputStream(), map);
        }catch (IOException exception) {
            System.out.println(exception.getMessage());
        }

    }
    static void message(HttpServletResponse response, HttpStatus status, String message) {
        try {
            Map<String, String> map = new HashMap<>();

            map.put("status", String.valueOf(HttpServletResponse.SC_OK));
            map.put("code", String.valueOf(HttpStatus.OK.value()));
            map.put("message", message);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(APPLICATION_JSON_VALUE);

            new MessageMapper(response.getOutputStream(), map);
        }catch (IOException exception) {
            System.out.println(exception.getMessage());
        }

    }

    static void projectIdentifierResponse(HttpStatus status, HttpServletResponse response, String message) {
        try {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("status", status.name());
            errorMap.put("code", String.valueOf(status.value()));
            errorMap.put("message", message);
            response.setStatus(status.value());
            response.setContentType(APPLICATION_JSON_VALUE);
            new MessageMapper(response.getOutputStream(), errorMap);
        }catch (IOException exception) {
            System.out.println(exception.getMessage());
        }

    }

    static void customData(HttpServletResponse response, Map<?, ?> map) {
        try {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(APPLICATION_JSON_VALUE);

            new MessageMapper(response.getOutputStream(), map);
        }catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
