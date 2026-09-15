package com.isaaca.musictracker.repositories;

import com.isaaca.musictracker.entities.ArtistReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistReviewRepository extends JpaRepository<ArtistReview, Integer> {
    Optional<ArtistReview> findArtistReviewByArtist_NameIgnoreCase(String name);
}
