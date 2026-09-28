package com.isaaca.musictracker.exceptions;

public class ArtistAlreadyReviewedException extends RuntimeException {

    public ArtistAlreadyReviewedException() {
        super();
    }

    public ArtistAlreadyReviewedException(String message) {
        super(message);
    }
}
