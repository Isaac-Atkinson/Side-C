package com.isaaca.musictracker.services;

import com.isaaca.musictracker.ArtistSearchResult;
import com.isaaca.musictracker.LastFMArtistResult;
import com.isaaca.musictracker.entities.Artist;
import com.isaaca.musictracker.entities.ArtistReview;
import com.isaaca.musictracker.exceptions.ArtistReviewNotFoundException;
import com.isaaca.musictracker.repositories.AlbumRepository;
import com.isaaca.musictracker.repositories.AlbumReviewRepository;
import com.isaaca.musictracker.repositories.ArtistRepository;
import com.isaaca.musictracker.repositories.ArtistReviewRepository;
import com.isaaca.musictracker.requests.ArtistRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Service
public class ArtistService {


    private final ArtistRepository artistRepository;
    private final LastFMService lastFMService;
    private final FanartService fanartService;
    private final ArtistReviewRepository artistReviewRepository;

    public ArtistService(
            AlbumRepository albumRepository,
            AlbumReviewRepository albumReviewRepository,
            ArtistRepository artistRepository,
            LastFMService lastFMService,
            FanartService fanartService,
            ArtistReviewRepository artistReviewRepository) {
        this.artistRepository = artistRepository;
        this.lastFMService = lastFMService;
        this.fanartService = fanartService;
        this.artistReviewRepository = artistReviewRepository;
    }

    public List<ArtistReview> getAllArtistReviews(){
        return artistReviewRepository.findAll();
    }

    public Optional<ArtistReview> getArtistReviewById(Integer id){
        return  artistReviewRepository.findById(id);
    }

    @Transactional
    public ArtistReview addArtistReview(ArtistRequest artistRequest) {
        Optional<Artist> existingArtist = artistRepository.findByMusicBrainzId(artistRequest.getMusicBrainzId());

        if(existingArtist.isEmpty()) {
            Artist artist = buildArtist(artistRequest);

            return buildArtistReview(artistRequest, artist);
        }
        Artist artist = existingArtist.get();

        return buildArtistReview(artistRequest, artist);
    }

    public ArtistReview updateArtistReview(Integer id, ArtistRequest artistRequest) {
        Optional<ArtistReview> optionalArtistReview = artistReviewRepository.findById(id);

        if(optionalArtistReview.isEmpty()) {
            throw new ArtistReviewNotFoundException();
        }

        ArtistReview artistReview = optionalArtistReview.get();

        artistReview.setRating(artistRequest.getRating());
        artistReview.setReviewText(artistRequest.getReviewText());
        return artistReviewRepository.save(artistReview);
    }

    public void deleteArtistReviewById(Integer id) {
        Optional<ArtistReview> optionalArtistReview = artistReviewRepository.findById(id);

        if(optionalArtistReview.isEmpty()) {
            throw new ArtistReviewNotFoundException();
        }

        artistReviewRepository.deleteById(id);
    }

    public List<ArtistSearchResult> getArtistSearchResults(String name) throws IOException, URISyntaxException, InterruptedException {
        List<LastFMArtistResult> artists = lastFMService.lookUpArtists(name);

        return artists.stream()
                .map(artist -> {

                    String imageUrl = null;

                    if (artist.musicBrainzId() != null &&
                            !artist.musicBrainzId().isBlank()) {

                        try {
                            imageUrl = fanartService.getFanartImageUrl(
                                    artist.musicBrainzId()
                            );
                        } catch (Exception e) {
                            imageUrl = null;
                        }
                    }

                    return new ArtistSearchResult(
                            artist.musicBrainzId(),
                            artist.name(),
                            imageUrl
                    );
                })
                .toList();
    }

    private Artist buildArtist(ArtistRequest artistRequest) {
        Artist artist = new Artist();

        artist.setName(artistRequest.getName());
        artist.setMusicBrainzId(artistRequest.getMusicBrainzId());
        artist.setImageUrl(artistRequest.getImageUrl());

        return artistRepository.save(artist);
    }

    private ArtistReview buildArtistReview(ArtistRequest artistRequest, Artist artist) {
        ArtistReview artistReview = new ArtistReview();
        artistReview.setArtist(artist);
        artistReview.setRating(artistRequest.getRating());
        artistReview.setReviewText(artistRequest.getReviewText());

        return artistReviewRepository.save(artistReview);
    }
}
