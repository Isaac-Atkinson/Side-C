package com.isaaca.musictracker.services;

import com.isaaca.musictracker.dto.response.search.AlbumSearchResult;
import com.isaaca.musictracker.dto.response.search.ArtistSearchResult;
import com.isaaca.musictracker.entities.AlbumReview;
import com.isaaca.musictracker.entities.ArtistReview;
import com.isaaca.musictracker.externalapi.lastfm.LastFMAlbumSearchResult;
import com.isaaca.musictracker.externalapi.lastfm.LastFMArtistResult;
import com.isaaca.musictracker.repositories.AlbumReviewRepository;
import com.isaaca.musictracker.repositories.ArtistReviewRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Service
public class SearchService {


    private final LastFMService lastFMService;
    private final FanartService fanartService;

    private final AlbumReviewRepository albumReviewRepository;
    private final ArtistReviewRepository artistReviewRepository;


    public SearchService(LastFMService lastFMService, FanartService fanartService, AlbumReviewRepository albumReviewRepository, ArtistReviewRepository artistReviewRepository) {
        this.lastFMService = lastFMService;
        this.fanartService = fanartService;
        this.albumReviewRepository = albumReviewRepository;
        this.artistReviewRepository = artistReviewRepository;
    }

    public List<AlbumSearchResult> searchAlbums(String title, String username) throws IOException, URISyntaxException, InterruptedException {
        List<LastFMAlbumSearchResult> lastFMResults = lastFMService.lookUpAlbums(title);

        return lastFMResults.stream()
                .map(album -> toAlbumSearchResult(album, username))
                .toList();
    }

    public List<ArtistSearchResult> searchArtists(String name, String username) throws IOException, URISyntaxException, InterruptedException {
        List<LastFMArtistResult> artists = lastFMService.lookUpArtists(name);

        return artists.stream()
                .map(artist -> toArtistSearchResult(artist, username))
                .toList();
    }


    private AlbumSearchResult toAlbumSearchResult(
            LastFMAlbumSearchResult album,
            String username
    ) {
        Optional<AlbumReview> existingReview =
                albumReviewRepository
                        .findByUser_UsernameAndAlbum_LastFMUrl(
                                username,
                                album.lastFMUrl()
                        );

        return new AlbumSearchResult(
                album.musicBrainzId(),
                album.lastFMUrl(),
                album.title(),
                album.artist(),
                album.imageUrl(),
                existingReview.isPresent(),
                existingReview.map(AlbumReview::getId).orElse(null)
        );
    }

    private ArtistSearchResult toArtistSearchResult(
            LastFMArtistResult artist,
            String username
    ) {
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

        Optional<ArtistReview> existingReview =
                artistReviewRepository
                        .findByUser_UsernameAndArtist_LastFMUrl(
                                username,
                                artist.lastFMUrl()
                        );

        return new ArtistSearchResult(
                artist.musicBrainzId(),
                artist.lastFMUrl(),
                artist.name(),
                imageUrl,
                existingReview.isPresent(),
                existingReview.map(ArtistReview::getId).orElse(null)
        );
    }
}
