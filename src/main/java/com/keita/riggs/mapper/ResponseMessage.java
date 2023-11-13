package com.keita.riggs.mapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseMessage {
    private long id;
    private String message;
    private String status;
    private int statusCode;

    public ResponseMessage(long id, String message, String status, int statusCode) {
        this.id = id;
        this.message = message;
        this.status = status;
        this.statusCode = statusCode;
    }
}
