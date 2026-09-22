package com.isaaca.musictracker;

import com.isaaca.musictracker.exceptions.AlbumReviewNotFoundException;
import com.isaaca.musictracker.exceptions.ArtistReviewNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.isaaca.musictracker.exceptions.AlbumAlreadyReviewedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AlbumAlreadyReviewedException.class)
    public ResponseEntity<String> handleAlbumAlreadyReviewed(
            AlbumAlreadyReviewedException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleAlbumReviewNotFound(
            AlbumReviewNotFoundException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleArtistReviewNotFound(
            ArtistReviewNotFoundException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
}
