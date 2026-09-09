package com.isaaca.musictracker.controllers;

import com.isaaca.musictracker.AlbumRequest;
import com.isaaca.musictracker.entities.Album;
import com.isaaca.musictracker.entities.Artist;
import com.isaaca.musictracker.repositories.AlbumRepository;
import com.isaaca.musictracker.repositories.ArtistRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<Album> getAlbums() {
        return albumRepository.findAll();
    }

    @PostMapping
    public Album addAlbum(@RequestBody AlbumRequest albumRequest) {
        String artistName = albumRequest.getArtist();

        Artist artist = artistRepository.findByNameIgnoreCase(artistName)
                .orElseGet(() -> {
                    Artist artist1 = new Artist();
                    artist1.setName(artistName);
                    return artistRepository.save(artist1);
                });

        Album album = new Album();
        album.setTitle(albumRequest.getTitle());
        album.setYear(albumRequest.getYear());
        album.setRating(albumRequest.getRating());
        album.setArtist(artist);

        return albumRepository.save(album);
    }

    @DeleteMapping
    public void deleteAll() {
        albumRepository.deleteAll();
        artistRepository.deleteAll();
    }

}
