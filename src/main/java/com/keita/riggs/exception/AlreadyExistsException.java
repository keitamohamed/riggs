package com.keita.riggs.exception;

public class AlreadyExistsException extends RuntimeException{

    public AlreadyExistsException(String msg)
    {
        super(msg);
    }
}
