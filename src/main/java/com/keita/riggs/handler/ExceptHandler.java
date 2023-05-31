package com.keita.riggs.handler;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

public class ExceptHandler extends RuntimeException{

    public ExceptHandler(HttpStatus status, HttpServletResponse response, String message) {
        super(message);
        MessageHandler.projectIdentifierResponse(status, response, message);
    }
}
