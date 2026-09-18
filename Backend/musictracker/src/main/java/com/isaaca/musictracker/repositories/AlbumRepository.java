package com.isaaca.musictracker.repositories;

import com.isaaca.musictracker.entities.Album;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Integer> {
    boolean existsByArtistId(Integer artistId);

    Optional<Album> findByUniqueIdentifier(String id);
}
