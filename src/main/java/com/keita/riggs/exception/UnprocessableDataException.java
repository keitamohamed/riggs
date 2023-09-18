package com.keita.riggs.exception;

import org.springframework.validation.BindingResult;

public class UnprocessableDataException extends RuntimeException {
    private BindingResult bindingResult;
    public UnprocessableDataException(String msg) {
        super(msg);
    }

    public UnprocessableDataException(String message, BindingResult bindingResult) {
        super(message);
        this.bindingResult = bindingResult;
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
