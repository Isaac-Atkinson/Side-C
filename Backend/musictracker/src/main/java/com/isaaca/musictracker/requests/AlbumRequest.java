package com.isaaca.musictracker.requests;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AlbumRequest {

    @NotBlank
    private String title;

    @NotNull
    private Integer year;

    @NotNull
    @Min(0)
    @Max(10)
    private Integer rating;

    @NotBlank
    private String artist;

    public String getTitle() {
        return title;
    }

    public Integer getYear() {
        return year;
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

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
