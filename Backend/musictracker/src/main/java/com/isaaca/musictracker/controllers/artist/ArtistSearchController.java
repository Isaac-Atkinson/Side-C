package com.isaaca.musictracker.controllers.artist;

import com.isaaca.musictracker.ArtistSearchResult;
import com.isaaca.musictracker.services.ArtistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/search/artists")
public class ArtistSearchController {

    private final ArtistService artistService;

    public ArtistSearchController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping
    public ResponseEntity<List<ArtistSearchResult>> getArtists(@RequestParam String name) throws IOException, URISyntaxException, InterruptedException {
        List<ArtistSearchResult> results = artistService.getArtistSearchResults(name);

        return ResponseEntity.ok(results);
    }
}
