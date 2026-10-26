package com.isaaca.musictracker.dto.response.review;

import com.isaaca.musictracker.dto.entities.AlbumDTO;
import com.isaaca.musictracker.entities.Album;
import com.isaaca.musictracker.entities.AlbumReview;

import java.time.Instant;
import java.time.LocalDate;

public record AlbumReviewResponse(
        Integer id,
        Integer rating,
        AlbumDTO album,
        String reviewText,
        Instant created,
        LocalDate listenedOn
) {
    public static AlbumReviewResponse fromAlbumReview(AlbumReview review) {
        return new AlbumReviewResponse(
                review.getId(),
                review.getRating(),
                AlbumDTO.from(review.getAlbum()),
                review.getReviewText(),
                review.getCreated(),
                review.getListenedOn()
        );
    }
}
