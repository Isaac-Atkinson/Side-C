package com.isaaca.musictracker.exceptions;

public class AlbumReviewNotFoundException extends RuntimeException{
    public AlbumReviewNotFoundException(){
        super("Album Review Not Found");
    }

    public AlbumReviewNotFoundException(String message){
        super(message);
    }
}
