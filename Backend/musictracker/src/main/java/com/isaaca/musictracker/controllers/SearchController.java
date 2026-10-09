package com.isaaca.musictracker.controllers;

import com.isaaca.musictracker.dto.SearchResult;
import com.isaaca.musictracker.services.AlbumService;
import com.isaaca.musictracker.services.ArtistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;

@CrossOrigin(origins = "http://localhost:5173",
        allowCredentials = "true")
@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final AlbumService albumService;
    private final ArtistService artistService;

    public SearchController(AlbumService albumService, ArtistService artistService) {
        this.albumService = albumService;
        this.artistService = artistService;
    }

    @GetMapping
    public ResponseEntity<SearchResult> search(@RequestParam String query) throws IOException, URISyntaxException, InterruptedException {

        SearchResult result = new SearchResult(
                albumService.getAlbumSearchResults(query),
                artistService.getArtistSearchResults(query)
        );

        return ResponseEntity.ok(result);
    }
}
