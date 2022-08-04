package com.spdu.app.hairdressing_salon.exception;

public class SalonDoesntExistException extends RuntimeException {
    public SalonDoesntExistException(String message) {
        super(message);
    }
}
