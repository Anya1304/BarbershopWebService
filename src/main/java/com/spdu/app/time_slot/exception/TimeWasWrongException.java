package com.spdu.app.time_slot.exception;

public class TimeWasWrongException extends RuntimeException {
    public TimeWasWrongException(String message) {
        super(message);
    }
}
