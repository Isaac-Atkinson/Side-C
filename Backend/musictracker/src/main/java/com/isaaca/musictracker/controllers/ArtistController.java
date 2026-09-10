package com.isaaca.musictracker.controllers;

import com.isaaca.musictracker.entities.Album;
import com.isaaca.musictracker.entities.Artist;
import com.isaaca.musictracker.repositories.AlbumRepository;
import com.isaaca.musictracker.repositories.ArtistRepository;
import com.isaaca.musictracker.requests.ArtistRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;

    public ArtistController(AlbumRepository albumRepository, ArtistRepository artistRepository) {
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
    }

    @GetMapping
    public List<Artist> getArtists() {
        return artistRepository.findAll();
    }

    @GetMapping("/{id")
    public ResponseEntity<Artist> getArtist(@PathVariable Integer id) {
        return artistRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Artist> addArtist(@Valid @RequestBody ArtistRequest artistRequest) {

        Artist artist = buildArtist(artistRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(artist);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Artist> updateArtist(@PathVariable Integer id, @Valid @RequestBody ArtistRequest artistRequest) {
        Optional<Artist> optionalArtist = artistRepository.findById(id);

        if(optionalArtist.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Artist artist = optionalArtist.get();

        artist.setName(artistRequest.getName());

        artistRepository.save(artist);
        return ResponseEntity.ok().body(artist);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtist(@PathVariable Integer id) {
        Optional<Artist> optionalArtist = artistRepository.findById(id);

        if(optionalArtist.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if(albumRepository.existsByArtistId(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        artistRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    private Artist buildArtist(ArtistRequest artistRequest) {
        Artist artist = new Artist();
        artist.setName(artistRequest.getName());
        artistRepository.save(artist);
        return artist;
    }
}
