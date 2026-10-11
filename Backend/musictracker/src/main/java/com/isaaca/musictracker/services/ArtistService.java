package com.isaaca.musictracker.services;

import com.isaaca.musictracker.dto.review.ArtistReviewResponse;
import com.isaaca.musictracker.dto.search.ArtistSearchResult;
import com.isaaca.musictracker.externalapi.lastfm.LastFMArtistResult;
import com.isaaca.musictracker.entities.Artist;
import com.isaaca.musictracker.entities.ArtistReview;
import com.isaaca.musictracker.entities.User;
import com.isaaca.musictracker.exceptions.ArtistAlreadyReviewedException;
import com.isaaca.musictracker.exceptions.ArtistReviewNotFoundException;
import com.isaaca.musictracker.repositories.*;
import com.isaaca.musictracker.requests.ArtistRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final UserRepository userRepository;

    public ArtistService(
            ArtistRepository artistRepository,
            LastFMService lastFMService,
            FanartService fanartService,
            ArtistReviewRepository artistReviewRepository,
            UserRepository userRepository) {
        this.artistRepository = artistRepository;
        this.lastFMService = lastFMService;
        this.fanartService = fanartService;
        this.artistReviewRepository = artistReviewRepository;
        this.userRepository = userRepository;
    }

    public List<ArtistReviewResponse> getAllArtistReviews(String username){
        return artistReviewRepository.findByUser_Username(username)
                .stream()
                .map(ArtistReviewResponse::fromArtistReview)
                .toList();
    }

    public Optional<ArtistReviewResponse> getArtistReviewById(Integer id,
                                                      String username){
        return  artistReviewRepository.findByIdAndUser_Username(id, username)
                .map(ArtistReviewResponse::fromArtistReview);
    }

    @Transactional
    public ArtistReviewResponse addArtistReview(ArtistRequest artistRequest, String username) {

        Optional<ArtistReview> existingArtistReview = artistReviewRepository
                .findByUser_UsernameAndArtist_MusicBrainzId(username,
                                                            artistRequest.getMusicBrainzId());

        if(existingArtistReview.isPresent()){
            throw new ArtistAlreadyReviewedException();
        }

        User user = userRepository
                .findUserByUsername(username)
                .orElseThrow();


        Optional<Artist> existingArtist = artistRepository.findByMusicBrainzId(artistRequest.getMusicBrainzId());

        if(existingArtist.isEmpty()) {
            Artist artist = buildArtist(artistRequest);

            ArtistReview review =  buildArtistReview(artistRequest, artist, user);

            return ArtistReviewResponse.fromArtistReview(review);
        }
        Artist artist = existingArtist.get();

        ArtistReview review =  buildArtistReview(artistRequest, artist, user);

        return ArtistReviewResponse.fromArtistReview(review);
    }

    public ArtistReviewResponse updateArtistReview(Integer id, ArtistRequest artistRequest, String username) {
        Optional<ArtistReview> optionalArtistReview = artistReviewRepository.findByIdAndUser_Username(id, username);

        if(optionalArtistReview.isEmpty()) {
            throw new ArtistReviewNotFoundException();
        }

        ArtistReview artistReview = optionalArtistReview.get();

        artistReview.setRating(artistRequest.getRating());
        artistReview.setReviewText(artistRequest.getReviewText());
        artistReviewRepository.save(artistReview);

        return ArtistReviewResponse.fromArtistReview(artistReview);
    }

    public void deleteArtistReviewById(Integer id, String username) {
        Optional<ArtistReview> optionalArtistReview = artistReviewRepository.findByIdAndUser_Username(id, username);

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

    private ArtistReview buildArtistReview(ArtistRequest artistRequest, Artist artist, User user) {
        ArtistReview artistReview = new ArtistReview();
        artistReview.setArtist(artist);
        artistReview.setUser(user);
        artistReview.setRating(artistRequest.getRating());
        artistReview.setReviewText(artistRequest.getReviewText());

        return artistReviewRepository.save(artistReview);
    }
}
