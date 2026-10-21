package com.isaaca.musictracker.repositories;

import com.isaaca.musictracker.entities.ArtistReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArtistReviewRepository extends JpaRepository<ArtistReview, Integer> {
    List<ArtistReview> findByUser_Username(String username);

    Optional<ArtistReview> findByIdAndUser_Username(
            Integer id,
            String username);

    Optional<ArtistReview> findByUser_UsernameAndArtist_LastFMUrl(
            String username, String lastFMUrl);
}
