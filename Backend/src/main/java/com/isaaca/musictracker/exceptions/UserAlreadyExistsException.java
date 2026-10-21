package com.isaaca.musictracker.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("Username Already Exists");
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
