package com.isaaca.musictracker.controllers.album;

import com.isaaca.musictracker.entities.AlbumReview;
import com.isaaca.musictracker.repositories.AlbumRepository;
import com.isaaca.musictracker.repositories.AlbumReviewRepository;
import com.isaaca.musictracker.repositories.ArtistRepository;
import com.isaaca.musictracker.requests.AlbumRequest;
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
    public ResponseEntity<List<AlbumReview>> getAllAlbumReviews(Authentication authentication) {

        String username = authentication.getName();

        return ResponseEntity.ok(albumService.getAllAlbumReviewsByUser(username));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumReview> getAlbumReviewById(
            @PathVariable Integer id,
            Authentication authentication) {

        return albumService.getAlbumReviewById(id, authentication.getName())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AlbumReview> addAlbumReview(@Valid @RequestBody AlbumRequest albumRequest, Authentication authentication)
            throws URISyntaxException, IOException, InterruptedException {

        String username = authentication.getName();

        AlbumReview albumReview = albumService.addAlbumreview(albumRequest, username);

        return ResponseEntity.status(HttpStatus.CREATED).body(albumReview);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlbumReview> updateAlbumReview(@PathVariable Integer id,
                                                         @Valid @RequestBody AlbumRequest albumRequest,
                                                         Authentication authentication)
            throws URISyntaxException, IOException, InterruptedException {

        String username = authentication.getName();

        return ResponseEntity.ok(albumService.updateAlbumReview(id, albumRequest, username));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>  deleteAlbumReviewById(@PathVariable Integer id,
                                                       Authentication authentication) {

        String username = authentication.getName();

        albumService.deleteAlbumReviewById(id, username);

        return ResponseEntity.noContent().build();
    }

}
