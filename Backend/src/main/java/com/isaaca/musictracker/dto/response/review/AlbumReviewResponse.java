package com.isaaca.musictracker.dto.response.review;

import com.isaaca.musictracker.entities.Album;
import com.isaaca.musictracker.entities.AlbumReview;

import java.time.Instant;

public record AlbumReviewResponse(
        Integer id,
        Integer rating,
        Album album,
        String reviewText,
        Instant created
) {
    public static AlbumReviewResponse fromAlbumReview(AlbumReview review) {
        return new AlbumReviewResponse(
                review.getId(),
                review.getRating(),
                review.getAlbum(),
                review.getReviewText(),
                review.getCreated()
        );
    }
}
