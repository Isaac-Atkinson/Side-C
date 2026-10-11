package com.isaaca.musictracker.controllers;

import com.isaaca.musictracker.dto.review.ArtistReviewResponse;
import com.isaaca.musictracker.entities.ArtistReview;
import com.isaaca.musictracker.requests.ArtistRequest;
import com.isaaca.musictracker.services.ArtistService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173",
        allowCredentials = "true")
@RestController
@RequestMapping("/api/artists/reviews")
public class ArtistReviewController {

    private final ArtistService artistService;

    public ArtistReviewController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping
    public ResponseEntity<List<ArtistReviewResponse>> getAllArtistReviews(Authentication authentication) {

        String username = authentication.getName();

        return ResponseEntity.ok(artistService.getAllArtistReviews(username));
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<ArtistReviewResponse> getArtistReviewById(@PathVariable Integer id,
                                                            Authentication authentication) {

        String username = authentication.getName();

        return artistService.getArtistReviewById(id, username)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ArtistReviewResponse> addArtistReview(@Valid @RequestBody ArtistRequest artistRequest,
                                                        Authentication authentication) {

        String username = authentication.getName();

        ArtistReviewResponse artistReview = artistService.addArtistReview(artistRequest, username);

        return ResponseEntity.status(HttpStatus.CREATED).body(artistReview);
    }

    @PutMapping({"/{id}"})
    public ResponseEntity<ArtistReviewResponse> updateArtistReview(@PathVariable Integer id,
                                                           @Valid @RequestBody ArtistRequest artistRequest,
                                                           Authentication authentication) {

        String username = authentication.getName();

        return ResponseEntity.ok(artistService.updateArtistReview(id, artistRequest, username));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtistReview(@PathVariable Integer id,
                                                   Authentication authentication){

        String username = authentication.getName();

        artistService.deleteArtistReviewById(id, username);

        return ResponseEntity.noContent().build();
    }





}
