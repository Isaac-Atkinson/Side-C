package com.isaaca.musictracker.repositories;

import com.isaaca.musictracker.entities.Album;
import com.isaaca.musictracker.entities.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Integer> {

    Optional<Artist> findByNameIgnoreCase(String name);

}
