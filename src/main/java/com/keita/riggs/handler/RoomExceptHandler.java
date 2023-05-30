package com.keita.riggs.handler;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

public class RoomExceptHandler extends RuntimeException{

    public RoomExceptHandler(HttpStatus status, HttpServletResponse response, String message) {
        super(message);
        MessageHandler.projectIdentifierResponse(status, response, message);
    }
}
