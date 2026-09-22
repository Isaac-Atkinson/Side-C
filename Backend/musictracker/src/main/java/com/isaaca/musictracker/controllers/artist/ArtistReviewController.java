package com.isaaca.musictracker.controllers.artist;

import com.isaaca.musictracker.entities.Artist;
import com.isaaca.musictracker.entities.ArtistReview;
import com.isaaca.musictracker.repositories.AlbumRepository;
import com.isaaca.musictracker.repositories.ArtistRepository;
import com.isaaca.musictracker.repositories.ArtistReviewRepository;
import com.isaaca.musictracker.requests.ArtistRequest;
import com.isaaca.musictracker.services.ArtistService;
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

    private final ArtistService artistService;

    public ArtistReviewController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping
    ResponseEntity<List<ArtistReview>> getAllArtistReviews() {
        return ResponseEntity.ok(artistService.getAllArtistReviews());
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<ArtistReview> getArtistReviewById(@PathVariable Integer id) {
        return artistService.getArtistReviewById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ArtistReview> addArtistReview(@Valid @RequestBody ArtistRequest artistRequest) {
        ArtistReview artistReview = artistService.addArtistReview(artistRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(artistReview);
    }

    @PutMapping({"/{id}"})
    public ResponseEntity<ArtistReview> updateArtistReview(@PathVariable Integer id, @Valid @RequestBody ArtistRequest artistRequest) {

        return ResponseEntity.ok(artistService.updateArtistReview(id, artistRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtistReview(@PathVariable Integer id){
        artistService.deleteArtistReviewById(id);

        return ResponseEntity.noContent().build();
    }





}
