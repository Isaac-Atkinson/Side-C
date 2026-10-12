package com.isaaca.musictracker.dto.request;

import com.isaaca.musictracker.constants.ReviewLimits;
import jakarta.validation.constraints.*;

public record AlbumReviewRequest(
        String musicBrainzId,

        @NotBlank
        String title,

        @NotBlank
        String artist,

        @NotNull
        @Min(ReviewLimits.MIN_RATING)
        @Max(ReviewLimits.MAX_RATING)
        Integer rating,

        @Size(max = ReviewLimits.MAX_REVIEW_LENGTH)
        String reviewText
) {
}
