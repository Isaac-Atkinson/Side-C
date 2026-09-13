package com.isaaca.musictracker.entities;

import jakarta.persistence.*;

@Entity
public class ArtistReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer rating;

    private String reviewText;

    @OneToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;

    public Long getId() {
        return id;
    }

    public Integer getRating() {
        return rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }
}
