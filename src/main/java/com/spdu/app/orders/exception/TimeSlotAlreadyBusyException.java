package com.spdu.app.orders.exception;

public class TimeSlotAlreadyBusyException extends RuntimeException {
    public TimeSlotAlreadyBusyException(String message) {
        super(message);
    }
}
