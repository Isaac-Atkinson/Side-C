package com.isaaca.musictracker.controllers;

import com.isaaca.musictracker.AlbumSearchResult;
import com.isaaca.musictracker.lastfm.albumsearch.LastFMAlbumSearchResponse;
import com.isaaca.musictracker.services.LastFMService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
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
