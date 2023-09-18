package com.keita.riggs.exception;

import org.springframework.validation.BindingResult;

public class UnprocessableRoomDataException extends RuntimeException{

    private BindingResult bindingResult;
    public UnprocessableRoomDataException(String msg) {
        super(msg);
    }

    public UnprocessableRoomDataException(String message, BindingResult bindingResult) {
        super(message);
        this.bindingResult = bindingResult;
    }

    public UnprocessableRoomDataException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }

    public void setBindingResult(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }
}
