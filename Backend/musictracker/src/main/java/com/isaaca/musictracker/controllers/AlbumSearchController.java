package com.isaaca.musictracker.controllers;

import com.isaaca.musictracker.AlbumSearchResult;
import com.isaaca.musictracker.lastfm.albumsearch.LastFMAlbumSearchResponse;
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

    @Value("${API_KEY}")
    private String API_KEY;

    @GetMapping
    public ResponseEntity<List<AlbumSearchResult>> getAlbums(@RequestParam String title) throws IOException, InterruptedException, URISyntaxException {
        HttpClient client = HttpClient.newHttpClient();

        String url =
                "https://ws.audioscrobbler.com/2.0/" +
                        "?method=album.search" +
                        "&album=" + URLEncoder.encode(title, StandardCharsets.UTF_8) +
                        "&api_key=" + API_KEY +
                        "&format=json";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header(
                        "User-Agent",
                        "MusicTracker/1.0 (iatkinson18@outlook.com)"
                )
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


        ObjectMapper mapper = new ObjectMapper();

        LastFMAlbumSearchResponse parsedResponse = mapper.readValue(response.body(), LastFMAlbumSearchResponse.class);

        List<AlbumSearchResult> results = parsedResponse
                .getLastFMResults()
                .getAlbumMatches()
                .getAlbums()
                .stream()
                .map(album -> new AlbumSearchResult(
                        album.getMbid(),
                        album.getName(),
                        album.getArtist(),
                        album.getImages().getLast().getImageUrl()
                )).toList();

        return ResponseEntity.ok(results);
    }



}
