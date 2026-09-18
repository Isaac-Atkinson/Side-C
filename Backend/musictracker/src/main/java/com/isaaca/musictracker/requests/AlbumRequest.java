package com.isaaca.musictracker.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AlbumRequest {

    @NotBlank
    private String musicBrainzId;

    @NotBlank
    private String title;

    @NotBlank
    private String artist;

    @NotNull
    @Min(0)
    @Max(10)
    private Integer rating;

    private String reviewText;

    public String getReviewText() {
        return reviewText;
    }

    public void setMusicBrainzId(String externalID) {
        this.musicBrainzId = externalID;
    }

    public String getMusicBrainzId() {
        return musicBrainzId;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getTitle() {
        return title;
    }

    public Integer getRating() {
        return rating;
    }

    public String getArtist() {
        return artist;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
