package com.isaaca.musictracker.exceptions;

public class AlbumReviewNotFoundException extends RuntimeException{
    public AlbumReviewNotFoundException(){
        super();
    }

    public AlbumReviewNotFoundException(String message){
        super(message);
    }
}
