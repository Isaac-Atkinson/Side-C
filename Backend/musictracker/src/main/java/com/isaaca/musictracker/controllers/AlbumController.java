package com.isaaca.musictracker.controllers;

import com.isaaca.musictracker.requests.AlbumRequest;
import com.isaaca.musictracker.entities.Album;
import com.isaaca.musictracker.entities.Artist;
import com.isaaca.musictracker.repositories.AlbumRepository;
import com.isaaca.musictracker.repositories.ArtistRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/albums")
public class AlbumController {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;

    public AlbumController(AlbumRepository albumRepository, ArtistRepository artistRepository) {
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
    }

    @GetMapping
    public ResponseEntity<List<Album>> getAlbums() {
        return ResponseEntity.ok(albumRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbumById(@PathVariable Integer id) {
        return albumRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Album> addAlbum(@Valid @RequestBody AlbumRequest albumRequest) {

        if(albumRepository.findByExternalId(albumRequest.getExternalId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Album album = buildAlbum(albumRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(album);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Album> updateAlbum(@PathVariable Integer id, @Valid @RequestBody AlbumRequest albumRequest) {
        Optional<Album> optionalAlbum = albumRepository.findById(id);

        if (optionalAlbum.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Album album = optionalAlbum.get();

        String artistName = albumRequest.getArtist();

        Artist artist = artistRepository.findByNameIgnoreCase(artistName)
                .orElseGet(() -> {
                    Artist artist1 = new Artist();
                    artist1.setName(artistName);
                    return artistRepository.save(artist1);
                });

        album.setTitle(albumRequest.getTitle());
        album.setArtist(artist);
        albumRepository.save(album);

        return ResponseEntity.ok().body(album);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Integer id) {
        Optional<Album> album = albumRepository.findById(id);

        if (album.isPresent()) {
            albumRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else return ResponseEntity.notFound().build();
    }


    /**
     * TEST ONLY!!!
     */
    @DeleteMapping
    public void deleteAll() {
        albumRepository.deleteAll();
    }

    private Album buildAlbum(AlbumRequest albumRequest) {
        String artistName = albumRequest.getArtist();

        Artist artist = artistRepository.findByNameIgnoreCase(artistName)
                .orElseGet(() -> {
                    Artist artist1 = new Artist();
                    artist1.setName(artistName);
                    return artistRepository.save(artist1);
                });

        Album album = new Album();
        album.setTitle(albumRequest.getTitle());
        album.setArtist(artist);

        return albumRepository.save(album);
    }

}
