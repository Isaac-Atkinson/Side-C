package com.isaaca.musictracker.requests;

import com.isaaca.musictracker.entities.Artist;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ArtistRequest {

    private String musicBrainzId;

    @NotBlank
    private String name;

    private String imageUrl;

    @NotNull
    @Min(0)
    @Max(10)
    private Integer rating;

    private String reviewText;

    public String getMusicBrainzId() {
        return musicBrainzId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
