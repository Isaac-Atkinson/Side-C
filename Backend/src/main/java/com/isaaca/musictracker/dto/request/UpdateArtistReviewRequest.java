package com.isaaca.musictracker.dto.request;

import com.isaaca.musictracker.constants.ReviewLimits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateArtistReviewRequest(
        @NotNull
        @Min(ReviewLimits.MIN_RATING)
        @Max(ReviewLimits.MAX_RATING)
        Integer rating,

        @Size(max = ReviewLimits.MAX_REVIEW_LENGTH)
        String reviewText
) {
}
