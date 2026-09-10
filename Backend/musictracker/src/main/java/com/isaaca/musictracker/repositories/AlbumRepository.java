package com.isaaca.musictracker.repositories;

import com.isaaca.musictracker.entities.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Integer> {
    boolean existsByArtistId(Integer artistId);
}
