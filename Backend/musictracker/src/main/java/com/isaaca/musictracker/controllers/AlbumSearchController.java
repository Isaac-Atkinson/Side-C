package com.isaaca.musictracker.controllers;

import com.isaaca.musictracker.musicbrainz.AlbumSearchResult;
import com.isaaca.musictracker.musicbrainz.MusicBrainzResponse;
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

    @GetMapping
    public ResponseEntity<List<AlbumSearchResult>> getAlbums(@RequestParam String title) throws InterruptedException, IOException, InterruptedException, URISyntaxException {
        HttpClient client = HttpClient.newHttpClient();
        String query =
                "releasegroup:\"" + title + "\" AND primarytype:album";

        String url =
                "https://musicbrainz.org/ws/2/release-group/" +
                        "?query=" +
                        URLEncoder.encode(query, StandardCharsets.UTF_8) +
                        "&fmt=json" +
                        "&limit=10";
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
        MusicBrainzResponse parsedResponse = mapper.readValue(response.body(), MusicBrainzResponse.class);

        List<AlbumSearchResult> results = parsedResponse
                .getReleaseGroups()
                .stream()
                .map(album -> new AlbumSearchResult(
                        album.getId(),
                        album.getTitle(),
                        album.getArtistCredits().getFirst().getName(),
                        extractYear(album.getFirstReleaseDate())
                )).toList();


        return ResponseEntity.ok(results);
    }

    private Integer extractYear(String date){
        if(date == null || date.length() < 4){
            return null;
        }
        return Integer.parseInt(date.substring(0, 4));
    }


}
