package com.isaaca.musictracker.dto.review;

import com.isaaca.musictracker.entities.Artist;
import com.isaaca.musictracker.entities.ArtistReview;

public record ArtistReviewResponse(
        Integer id,
        Integer rating,
        Artist artist,
        String reviewText
) {
    public static ArtistReviewResponse fromArtistReview(ArtistReview review) {
        return new ArtistReviewResponse(
                review.getId(),
                review.getRating(),
                review.getArtist(),
                review.getReviewText()
        );
    }
}
