package com.isaaca.musictracker.controllers;

import com.isaaca.musictracker.entities.Album;
import com.isaaca.musictracker.entities.AlbumReview;
import com.isaaca.musictracker.entities.Artist;
import com.isaaca.musictracker.lastfm.albuminfo.LastFMResponseAlbumInfo;
import com.isaaca.musictracker.repositories.AlbumRepository;
import com.isaaca.musictracker.repositories.AlbumReviewRepository;
import com.isaaca.musictracker.repositories.ArtistRepository;
import com.isaaca.musictracker.requests.AlbumRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/albums/reviews")
public class AlbumReviewController {

    @Value("${API_KEY}")
    private String API_KEY;

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final AlbumReviewRepository albumReviewRepository;

    public AlbumReviewController(AlbumRepository albumRepository, ArtistRepository artistRepository, AlbumReviewRepository albumReviewRepository) {
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
        this.albumReviewRepository = albumReviewRepository;

    }

    @GetMapping
    public ResponseEntity<List<AlbumReview>> getAllAlbumReviews() {
        return ResponseEntity.ok(albumReviewRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumReview> getAlbumReviewById(@PathVariable Integer id) {
        return albumReviewRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AlbumReview> addAlbumReview(@Valid @RequestBody AlbumRequest albumRequest) throws URISyntaxException, IOException, InterruptedException {

        String uniqueId = "";
        if(albumRequest.getMusicBrainzId().equals("")){
            albumRequest.setMusicBrainzId(null);
            uniqueId = albumRequest.getTitle() + albumRequest.getArtist();
        } else {
            uniqueId = albumRequest.getMusicBrainzId();
        }

        Optional<AlbumReview> optionalAlbumReview = albumReviewRepository.findByAlbum_UniqueIdentifier(uniqueId);
        if(optionalAlbumReview.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(optionalAlbumReview.get());
        }

        Optional<Album> optionalAlbum = albumRepository.findByUniqueIdentifier(uniqueId);

        if (optionalAlbum.isEmpty()){
            Album album = buildAlbum(albumRequest, uniqueId);

            AlbumReview albumReview = buildAlbumReview(albumRequest, album);

            return ResponseEntity.status(HttpStatus.CREATED).body(albumReviewRepository.save(albumReview));
        }

        Album album = optionalAlbum.get();

        AlbumReview albumReview = buildAlbumReview(albumRequest, album);

        return ResponseEntity.status(HttpStatus.CREATED).body(albumReviewRepository.save(albumReview));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlbumReview> updateAlbumReview(@PathVariable Integer id, @Valid @RequestBody AlbumRequest albumRequest) {
        Optional<AlbumReview> optionalAlbumReview = albumReviewRepository.findById(id);


        if (optionalAlbumReview.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        AlbumReview albumReview = optionalAlbumReview.get();
        albumReview.setRating(albumRequest.getRating());
        albumReview.setReviewText(albumRequest.getReviewText());

        return ResponseEntity.ok(albumReviewRepository.save(albumReview));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>  deleteAlbumReviewById(@PathVariable Integer id) {
        Optional<AlbumReview> optionalAlbumReview = albumReviewRepository.findById(id);

        if (optionalAlbumReview.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        albumReviewRepository.deleteById(optionalAlbumReview.get().getId());
        return ResponseEntity.noContent().build();
    }

    private Album buildAlbum(AlbumRequest albumRequest, String uniqueId) throws URISyntaxException, IOException, InterruptedException {
        String artistName = albumRequest.getArtist();

        Artist artist = artistRepository.findByNameIgnoreCase(artistName)
                .orElseGet(() -> {
                    Artist artist1 = new Artist();
                    artist1.setName(artistName);
                    return artistRepository.save(artist1);
                });

        Album album = new Album();

        album.setTitle(albumRequest.getTitle());
        album.setArtist(artist);
        album.setMusicBrainzId(albumRequest.getMusicBrainzId());


        String url = "";
        HttpClient client = HttpClient.newHttpClient();
        if(albumRequest.getMusicBrainzId().equals(uniqueId)){


            url =
                    "https://ws.audioscrobbler.com/2.0/" +
                            "?method=album.getinfo" +
                            "&mbid=" + URLEncoder.encode(albumRequest.getMusicBrainzId(), StandardCharsets.UTF_8) +
                            "&api_key=" + API_KEY +
                            "&format=json";

        } else {
            url =
                    "https://ws.audioscrobbler.com/2.0/" +
                            "?method=album.getinfo" +
                            "&album=" + URLEncoder.encode(albumRequest.getTitle(), StandardCharsets.UTF_8) +
                            "&artist=" + URLEncoder.encode(albumRequest.getArtist(), StandardCharsets.UTF_8) +
                            "&api_key=" + API_KEY +
                            "&format=json";
        }

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

        LastFMResponseAlbumInfo parsedResponse = mapper.readValue(response.body(), LastFMResponseAlbumInfo.class);

        String imageUrl = parsedResponse
                .getAlbum()
                .getImages()
                .getLast()
                .getImageUrl();

        album.setImageUrl(imageUrl);

        return albumRepository.save(album);
    }

    private AlbumReview buildAlbumReview(AlbumRequest albumRequest, Album album) {
        AlbumReview albumReview = new AlbumReview();
        albumReview.setAlbum(album);
        albumReview.setRating(albumRequest.getRating());
        albumReview.setReviewText(albumRequest.getReviewText());

        return albumReviewRepository.save(albumReview);
    }
}
