package com.isaaca.musictracker.exceptions;

public class ArtistReviewNotFoundException extends RuntimeException {
    public ArtistReviewNotFoundException() {
        super();
    }

    public ArtistReviewNotFoundException(String message) {
        super(message);
    }
}
