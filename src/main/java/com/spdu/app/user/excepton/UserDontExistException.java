package com.spdu.app.user.excepton;

public class UserDontExistException extends RuntimeException {
    public UserDontExistException(String message) {
        super(message);
    }
}
