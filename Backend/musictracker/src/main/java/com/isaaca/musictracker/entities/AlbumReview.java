package com.isaaca.musictracker.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
public class AlbumReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Min(0)
    @Max(10)
    private Integer rating;

    @OneToOne
    @JoinColumn(name = "album_id")
    private Album album;

    private String reviewText;

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public Integer getId() {
        return id;
    }

    public Integer getRating() {
        return rating;
    }

    public Album getAlbum() {
        return album;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}
