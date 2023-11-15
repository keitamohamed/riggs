package com.keita.riggs.exception;

import org.springframework.validation.BindingResult;

public class UnprocessableDataException extends RuntimeException {
    private BindingResult bindingResult;
    private String message;
    public UnprocessableDataException(String msg) {
        super(msg);
    }

    public UnprocessableDataException(String message, BindingResult bindingResult) {
        super(message);
        this.message = message;
        this.bindingResult = bindingResult;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UnprocessableDataException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }


    public BindingResult getBindingResult() {
        return bindingResult;
    }

    public void setBindingResult(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }
}
