package com.isaaca.musictracker.controllers.album;

import com.isaaca.musictracker.AlbumSearchResult;
import com.isaaca.musictracker.services.LastFMService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/search/albums")
public class AlbumSearchController {

    private final LastFMService lastFMService;

    public AlbumSearchController(LastFMService lastFMService) {
        this.lastFMService = lastFMService;
    }

    @GetMapping
    public ResponseEntity<List<AlbumSearchResult>> getAlbums(@RequestParam String title) throws IOException, InterruptedException, URISyntaxException {

        List<AlbumSearchResult> results = lastFMService.lookUpAlbums(title);

        return ResponseEntity.ok(results);
    }



}
