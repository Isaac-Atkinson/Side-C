package com.isaaca.musictracker.dto.request;

import com.isaaca.musictracker.constants.ReviewLimits;
import jakarta.validation.constraints.*;

public record UpdateAlbumReviewRequest(
        @NotNull
        @Min(ReviewLimits.MIN_RATING)
        @Max(ReviewLimits.MAX_RATING)
        Integer rating,

        @Size(max = ReviewLimits.MAX_REVIEW_LENGTH)
        String reviewText,

        @NotBlank
        String listenedOn
) {
}
