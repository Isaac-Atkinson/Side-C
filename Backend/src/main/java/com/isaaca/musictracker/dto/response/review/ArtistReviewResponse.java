package com.isaaca.musictracker.dto.response.review;

import com.isaaca.musictracker.dto.entities.ArtistDTO;
import com.isaaca.musictracker.entities.Artist;
import com.isaaca.musictracker.entities.ArtistReview;

import java.time.Instant;

public record ArtistReviewResponse(
        Integer id,
        Integer rating,
        ArtistDTO artist,
        String reviewText,
        Instant created
) {
    public static ArtistReviewResponse fromArtistReview(ArtistReview review) {
        return new ArtistReviewResponse(
                review.getId(),
                review.getRating(),
                ArtistDTO.from(review.getArtist()),
                review.getReviewText(),
                review.getCreated()
        );
    }
}
