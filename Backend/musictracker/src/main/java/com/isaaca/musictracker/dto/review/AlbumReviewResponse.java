package com.isaaca.musictracker.dto.review;

import com.isaaca.musictracker.entities.Album;
import com.isaaca.musictracker.entities.AlbumReview;

public record AlbumReviewResponse(
        Integer id,
        Integer rating,
        Album album,
        String reviewText
) {
    public static AlbumReviewResponse fromAlbumReview(AlbumReview review) {
        return new AlbumReviewResponse(
                review.getId(),
                review.getRating(),
                review.getAlbum(),
                review.getReviewText()
        );
    }
}
