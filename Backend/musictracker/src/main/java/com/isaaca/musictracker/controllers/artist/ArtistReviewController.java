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
@RequestMapping("/api/artists/reviews")
public class ArtistReviewController {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final ArtistReviewRepository artistReviewRepository;

    public ArtistReviewController(AlbumRepository albumRepository, ArtistRepository artistRepository, ArtistReviewRepository artistReviewRepository) {
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
        this.artistReviewRepository = artistReviewRepository;
    }

    @GetMapping
    ResponseEntity<List<ArtistReview>> getAllArtistReviews() {
        return ResponseEntity.ok(artistReviewRepository.findAll());
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<ArtistReview> getArtistReviewById(@PathVariable Integer id) {
        return artistReviewRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ArtistReview> addArtistReview(@Valid @RequestBody ArtistRequest artistRequest) {
        Optional<ArtistReview> optionalArtistReview = artistReviewRepository.findArtistReviewByArtist_NameIgnoreCase(artistRequest.getName());

        if (optionalArtistReview.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(optionalArtistReview.get());
        }

        Optional<Artist> existingArtist = artistRepository.findByNameIgnoreCase(artistRequest.getName());

        if(existingArtist.isEmpty()) {
            Artist artist = buildArtist(artistRequest);

            return ResponseEntity.status(HttpStatus.CREATED).body(buildArtistReview(artistRequest, artist));
        }
        Artist artist = existingArtist.get();

        return ResponseEntity.status(HttpStatus.CREATED).body(buildArtistReview(artistRequest, artist));
    }

    @PutMapping({"/{id}"})
    public ResponseEntity<ArtistReview> updateArtistReview(@PathVariable Integer id, @Valid @RequestBody ArtistRequest artistRequest) {
        Optional<ArtistReview> optionalArtistReview = artistReviewRepository.findById(id);

        if(optionalArtistReview.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ArtistReview artistReview = optionalArtistReview.get();

        artistReview.setRating(artistRequest.getRating());
        artistReview.setReviewText(artistRequest.getReviewText());
        artistReviewRepository.save(artistReview);
        return ResponseEntity.ok(artistReview);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtistReview(@PathVariable Integer id) throws  Exception{
        Optional<ArtistReview> optionalArtistReview = artistReviewRepository.findById(id);
        if(optionalArtistReview.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ArtistReview artistReview = optionalArtistReview.get();
        artistReviewRepository.delete(artistReview);
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
