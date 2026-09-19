package com.isaaca.musictracker.controllers.artist;

import com.isaaca.musictracker.entities.Artist;
import com.isaaca.musictracker.entities.ArtistReview;
import com.isaaca.musictracker.repositories.AlbumRepository;
import com.isaaca.musictracker.repositories.ArtistRepository;
import com.isaaca.musictracker.repositories.ArtistReviewRepository;
import com.isaaca.musictracker.requests.ArtistRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final ArtistReviewRepository artistReviewRepository;

    public ArtistController(AlbumRepository albumRepository, ArtistRepository artistRepository, ArtistReviewRepository artistReviewRepository) {
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
        this.artistReviewRepository = artistReviewRepository;
    }

    @GetMapping
    public ResponseEntity<List<Artist>> getArtists() {
        return ResponseEntity.ok(artistRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artist> getArtist(@PathVariable Integer id) {
        return artistRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Artist> addArtist(@Valid @RequestBody ArtistRequest artistRequest) {

        if(artistRepository.findByNameIgnoreCase(artistRequest.getName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

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
        return ResponseEntity.noContent().build();
    }

    private Artist buildArtist(ArtistRequest artistRequest) {
        Artist artist = new Artist();
        artist.setName(artistRequest.getName());
        return artistRepository.save(artist);
    }

    private ArtistReview buildArtistReview(ArtistRequest artistRequest, Artist artist) {
        ArtistReview artistReview = new ArtistReview();
        artistReview.setArtist(artist);
        artistReview.setRating(artistRequest.getRating());
        artistReview.setReviewText(artistRequest.getReviewText());

        return artistReviewRepository.save(artistReview);
    }
}
