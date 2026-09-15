package com.isaaca.musictracker.controllers;

import com.isaaca.musictracker.entities.Album;
import com.isaaca.musictracker.entities.AlbumReview;
import com.isaaca.musictracker.entities.Artist;
import com.isaaca.musictracker.repositories.AlbumRepository;
import com.isaaca.musictracker.repositories.AlbumReviewRepository;
import com.isaaca.musictracker.repositories.ArtistRepository;
import com.isaaca.musictracker.requests.AlbumRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/albums/reviews")
public class AlbumReviewController {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final AlbumReviewRepository albumReviewRepository;

    public AlbumReviewController(AlbumRepository albumRepository, ArtistRepository artistRepository, AlbumReviewRepository albumReviewRepository) {
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
        this.albumReviewRepository = albumReviewRepository;

    }

    @GetMapping
    public ResponseEntity<List<AlbumReview>> getAllAlbumReviews() {
        return ResponseEntity.ok(albumReviewRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumReview> getAlbumReviewById(@PathVariable Integer id) {
        return albumReviewRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AlbumReview> addAlbumReview(@Valid @RequestBody AlbumRequest albumRequest) {
        Optional<AlbumReview> optionalAlbumReview = albumReviewRepository.findByAlbum_TitleIgnoreCase(albumRequest.getTitle());
        if(optionalAlbumReview.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(optionalAlbumReview.get());
        }

        Optional<Album> optionalAlbum = albumRepository.findByTitleIgnoreCaseAndArtist_NameIgnoreCase(
                albumRequest.getTitle(),
                albumRequest.getArtist());



        if (optionalAlbum.isEmpty()){
            Album album = buildAlbum(albumRequest);

            AlbumReview albumReview = buildAlbumReview(albumRequest, album);

            return ResponseEntity.status(HttpStatus.CREATED).body(albumReviewRepository.save(albumReview));
        }

        Album album = optionalAlbum.get();

        AlbumReview albumReview = buildAlbumReview(albumRequest, album);

        return ResponseEntity.status(HttpStatus.CREATED).body(albumReviewRepository.save(albumReview));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlbumReview> updateAlbumReview(@PathVariable Integer id, @Valid @RequestBody AlbumRequest albumRequest) {
        Optional<AlbumReview> optionalAlbumReview = albumReviewRepository.findById(id);


        if (optionalAlbumReview.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        AlbumReview albumReview = optionalAlbumReview.get();
        albumReview.setRating(albumRequest.getRating());
        albumReview.setReviewText(albumRequest.getReviewText());

        return ResponseEntity.ok(albumReviewRepository.save(albumReview));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>  deleteAlbumReviewById(@PathVariable Integer id) {
        Optional<AlbumReview> optionalAlbumReview = albumReviewRepository.findById(id);

        if (optionalAlbumReview.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        albumReviewRepository.deleteById(optionalAlbumReview.get().getId());
        return ResponseEntity.noContent().build();
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
        album.setYear(albumRequest.getYear());
        album.setArtist(artist);

        return albumRepository.save(album);
    }

    private AlbumReview buildAlbumReview(AlbumRequest albumRequest, Album album) {
        AlbumReview albumReview = new AlbumReview();
        albumReview.setAlbum(album);
        albumReview.setRating(albumRequest.getRating());
        albumReview.setReviewText(albumRequest.getReviewText());

        return albumReviewRepository.save(albumReview);
    }
}
