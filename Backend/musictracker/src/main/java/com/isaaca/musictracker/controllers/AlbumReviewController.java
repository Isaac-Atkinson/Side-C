package com.isaaca.musictracker.controllers;

import com.isaaca.musictracker.dto.request.AlbumReviewRequest;
import com.isaaca.musictracker.dto.request.UpdateAlbumReviewRequest;
import com.isaaca.musictracker.dto.response.review.AlbumReviewResponse;
import com.isaaca.musictracker.services.AlbumService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173",
        allowCredentials = "true")
@RestController
@RequestMapping("/api/albums/reviews")
public class AlbumReviewController {

    private final AlbumService albumService;


    public AlbumReviewController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping
    public ResponseEntity<List<AlbumReviewResponse>> getAllAlbumReviews(Authentication authentication) {

        String username = authentication.getName();

        return ResponseEntity.ok(albumService.getAllAlbumReviewsByUser(username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumReviewResponse> getAlbumReviewById(
            @PathVariable Integer id,
            Authentication authentication) {

        return albumService.getAlbumReviewById(id, authentication.getName())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AlbumReviewResponse> addAlbumReview(@Valid @RequestBody AlbumReviewRequest albumRequest, Authentication authentication)
            throws URISyntaxException, IOException, InterruptedException {

        String username = authentication.getName();

        AlbumReviewResponse albumReview = albumService.addAlbumReview(albumRequest, username);

        return ResponseEntity.status(HttpStatus.CREATED).body(albumReview);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlbumReviewResponse> updateAlbumReview(@PathVariable Integer id,
                                                         @Valid @RequestBody UpdateAlbumReviewRequest albumRequest,
                                                         Authentication authentication) {

        String username = authentication.getName();

        return ResponseEntity.ok(albumService.updateAlbumReview(id, albumRequest, username));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>  deleteAlbumReviewById(@PathVariable Integer id,
                                                       Authentication authentication) {

        String username = authentication.getName();

        albumService.deleteAlbumReview(id, username);

        return ResponseEntity.noContent().build();
    }

}
