package com.isaaca.musictracker.repositories;

import com.isaaca.musictracker.entities.ArtistReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistReviewRepository extends JpaRepository<ArtistReview, Integer> {
}
