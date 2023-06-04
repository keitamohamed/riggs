package com.keita.riggs.handler;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

public class ErrorMessage {

    public ErrorMessage(HttpServletResponse response, String message) {
        MessageHandler.message(response, message);
    }
}
