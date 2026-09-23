package com.isaaca.musictracker.exceptions;

public class EmailAlreadyInUseException extends RuntimeException{
    public EmailAlreadyInUseException() {
        super("Email Already In Use");
    }
    public EmailAlreadyInUseException(String message){
        super(message);
    }


}
