package com.isaaca.musictracker.repositories;

import com.isaaca.musictracker.entities.Album;
import com.isaaca.musictracker.entities.AlbumReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AlbumReviewRepository extends JpaRepository<AlbumReview, Integer> {

    List<AlbumReview> findByUser_Username(String username);

    Optional<AlbumReview> findByIdAndUser_Username(
            Integer id,
            String username
    );

    Optional<AlbumReview> findByUser_UsernameAndAlbum_LastFMUrl(String userUsername, String albumLastFMUrl);
}
