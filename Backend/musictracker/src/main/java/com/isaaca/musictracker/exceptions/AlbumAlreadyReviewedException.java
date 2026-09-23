package com.isaaca.musictracker.exceptions;

public class AlbumAlreadyReviewedException extends RuntimeException {
    public AlbumAlreadyReviewedException() {
        super("Album Already Reviewed");
    }

    public AlbumAlreadyReviewedException(String message) {
        super(message);
    }
}
