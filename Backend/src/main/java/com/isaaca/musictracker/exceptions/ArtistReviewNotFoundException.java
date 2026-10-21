package com.isaaca.musictracker.exceptions;

public class ArtistReviewNotFoundException extends RuntimeException {
    public ArtistReviewNotFoundException() {
        super("Artist Review Not Found");
    }

    public ArtistReviewNotFoundException(String message) {
        super(message);
    }
}
