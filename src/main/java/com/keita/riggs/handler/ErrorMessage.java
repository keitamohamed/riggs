package com.keita.riggs.handler;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

public class ErrorMessage extends RuntimeException{

    public ErrorMessage(HttpServletResponse response, HttpStatus status, String message) {
        super(message);
        MessageHandler.message(response, status, message);
    }
}
