package com.isaaca.musictracker.exceptions;

public class AlbumAlreadyReviewedException extends RuntimeException {
    public AlbumAlreadyReviewedException() {
        super("Album already reviewed");
    }

    public AlbumAlreadyReviewedException(String message) {
        super(message);
    }
}
