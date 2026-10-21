package com.isaaca.musictracker.controllers;

import com.isaaca.musictracker.dto.response.search.SearchResult;
import com.isaaca.musictracker.services.AlbumService;
import com.isaaca.musictracker.services.ArtistService;
import com.isaaca.musictracker.services.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;


@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(AlbumService albumService, ArtistService artistService, SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public ResponseEntity<SearchResult> search(@RequestParam String query, Authentication authentication) throws IOException, URISyntaxException, InterruptedException {

        String username = authentication.getName();

        SearchResult result = new SearchResult(
                searchService.searchAlbums(query, username),
                searchService.searchArtists(query, username)
        );

        return ResponseEntity.ok(result);
    }
}
