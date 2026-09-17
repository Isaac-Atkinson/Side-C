package com.isaaca.musictracker.repositories;

import com.isaaca.musictracker.entities.Album;
import com.isaaca.musictracker.entities.AlbumReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AlbumReviewRepository extends JpaRepository<AlbumReview, Integer> {
    Optional<AlbumReview> findByAlbum_ExternalId(String id);
}
