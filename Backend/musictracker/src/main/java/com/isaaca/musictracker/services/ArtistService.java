package com.isaaca.musictracker.services;

import com.isaaca.musictracker.ArtistSearchResult;
import com.isaaca.musictracker.LastFMArtistResult;
import com.isaaca.musictracker.repositories.AlbumRepository;
import com.isaaca.musictracker.repositories.AlbumReviewRepository;
import com.isaaca.musictracker.repositories.ArtistRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArtistService {

    private final AlbumRepository albumRepository;
    private final AlbumReviewRepository albumReviewRepository;
    private final ArtistRepository artistRepository;
    private final LastFMService lastFMService;
    private final FanartService fanartService;

    public ArtistService(
            AlbumRepository albumRepository,
            AlbumReviewRepository albumReviewRepository,
            ArtistRepository artistRepository,
            LastFMService lastFMService,
            FanartService fanartService
    ) {
        this.albumRepository = albumRepository;
        this.albumReviewRepository = albumReviewRepository;
        this.artistRepository = artistRepository;
        this.lastFMService = lastFMService;
        this.fanartService = fanartService;
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
}
