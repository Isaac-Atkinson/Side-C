package com.isaaca.musictracker.dto.request;

import com.isaaca.musictracker.constants.ReviewLimits;
import jakarta.validation.constraints.*;

public record ArtistReviewRequest(
        String musicBrainzId,

        @NotBlank
        String name,

        String imageUrl,

        @NotNull
        @Min(ReviewLimits.MIN_RATING)
        @Max(ReviewLimits.MAX_RATING)
        Integer rating,

        @Size(max = ReviewLimits.MAX_REVIEW_LENGTH)
        String reviewText
) {
}
