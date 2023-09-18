package com.keita.riggs.exception;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class ErrorResponse {
    private int statusCode;
    private String message;
    private String timestamp;
    private HttpStatus httpStatus;
    private Map<String, Object> map;

    public ErrorResponse(int statusCode, String message, String timestamp, HttpStatus httpStatus) {
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = timestamp;
        this.httpStatus = httpStatus;
    }

    public ErrorResponse(int statusCode, String message, String timestamp, HttpStatus httpStatus, Map<String, Object> map) {
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = timestamp;
        this.httpStatus = httpStatus;
        this.map = map;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}

