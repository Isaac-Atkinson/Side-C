package com.isaaca.musictracker.entities;

import com.isaaca.musictracker.constants.ReviewLimits;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.time.LocalDate;

@Entity
public class AlbumReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    @NotNull
    @Min(ReviewLimits.MIN_RATING)
    @Max(ReviewLimits.MAX_RATING)
    private Integer rating;

    @Size(max = ReviewLimits.MAX_REVIEW_LENGTH)
    private String reviewText;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private Instant created;

    @NotNull
    private LocalDate listenedOn;

    public LocalDate getListenedOn() {
        return listenedOn;
    }

    public void setListenedOn(LocalDate listenedOn) {
        this.listenedOn = listenedOn;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
