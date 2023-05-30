package com.keita.riggs.handler;

import jakarta.servlet.http.HttpServletResponse;

public class SuccessfulRequest {

    public SuccessfulRequest(HttpServletResponse response, String message) {
        MessageHandler.message(response, message);
    }
}
