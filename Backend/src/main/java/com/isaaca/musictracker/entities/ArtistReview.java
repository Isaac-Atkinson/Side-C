package com.isaaca.musictracker.entities;

import com.isaaca.musictracker.constants.ReviewLimits;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

@Entity
public class ArtistReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @NotNull
    @Min(ReviewLimits.MIN_RATING)
    @Max(ReviewLimits.MAX_RATING)
    private Integer rating;

    @Size(max = ReviewLimits.MAX_REVIEW_LENGTH)
    private String reviewText;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Instant created;

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
