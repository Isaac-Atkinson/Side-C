package com.isaaca.musictracker.services;

import com.isaaca.musictracker.dto.request.AlbumReviewRequest;
import com.isaaca.musictracker.dto.request.UpdateAlbumReviewRequest;
import com.isaaca.musictracker.dto.response.review.AlbumReviewResponse;
import com.isaaca.musictracker.dto.response.search.AlbumSearchResult;
import com.isaaca.musictracker.entities.Album;
import com.isaaca.musictracker.entities.AlbumReview;
import com.isaaca.musictracker.entities.Artist;
import com.isaaca.musictracker.entities.User;
import com.isaaca.musictracker.exceptions.AlbumAlreadyReviewedException;
import com.isaaca.musictracker.exceptions.AlbumReviewNotFoundException;
import com.isaaca.musictracker.externalapi.lastfm.albuminfo.LastFMAlbumInfo;
import com.isaaca.musictracker.externalapi.lastfm.artistinfo.LastFMArtistInfo;
import com.isaaca.musictracker.repositories.AlbumRepository;
import com.isaaca.musictracker.repositories.AlbumReviewRepository;
import com.isaaca.musictracker.repositories.ArtistRepository;
import com.isaaca.musictracker.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class AlbumService {


    private final FanartService fanartService;
    private final AlbumRepository albumRepository;
    private final AlbumReviewRepository albumReviewRepository;
    private final ArtistRepository artistRepository;
    private final LastFMService lastFMService;
    private final UserRepository userRepository;

    public AlbumService(
            AlbumRepository albumRepository,
            AlbumReviewRepository albumReviewRepository,
            ArtistRepository artistRepository,
            LastFMService lastFMService,
            UserRepository userRepository, FanartService fanartService) {
        this.albumRepository = albumRepository;
        this.albumReviewRepository = albumReviewRepository;
        this.artistRepository = artistRepository;
        this.lastFMService = lastFMService;
        this.userRepository = userRepository;
        this.fanartService = fanartService;
    }

    public List<AlbumReviewResponse> getAllAlbumReviewsByUser(String username)  {

        return albumReviewRepository.findByUser_Username(username)
                .stream()
                .map(AlbumReviewResponse::fromAlbumReview)
                .toList();
    }

    public Optional<AlbumReviewResponse> getAlbumReviewById(Integer id, String username)  {
        return albumReviewRepository.findByIdAndUser_Username(id, username)
                .map(AlbumReviewResponse::fromAlbumReview);
    }

    @Transactional
    public AlbumReviewResponse addAlbumReview(AlbumReviewRequest request, String username)
            throws URISyntaxException, IOException, InterruptedException {

        User user = userRepository
                .findUserByUsername(username)
                .orElseThrow();


        Optional<AlbumReview> existingAlbumReview = albumReviewRepository
                .findByUser_UsernameAndAlbum_LastFMUrl(username, request.lastFMUrl());

        if(existingAlbumReview.isPresent()) {
            throw new AlbumAlreadyReviewedException();
        }

        Optional<Album> optionalAlbum = albumRepository.findByLastFMUrl(request.lastFMUrl());

        if (optionalAlbum.isEmpty()){
            Album album = buildAlbum(request);
            albumRepository.save(album);

            AlbumReview albumReview = buildAlbumReview(request, album, user);

            albumReviewRepository.save(albumReview);

            return AlbumReviewResponse.fromAlbumReview(albumReview);
        }

        Album album = optionalAlbum.get();

        AlbumReview albumReview = buildAlbumReview(request, album, user);

        albumReviewRepository.save(albumReview);

        return AlbumReviewResponse.fromAlbumReview(albumReview);
    }

    public AlbumReviewResponse updateAlbumReview(Integer id, UpdateAlbumReviewRequest request, String username) {
        Optional<AlbumReview> optionalAlbumReview = albumReviewRepository.findByIdAndUser_Username(id, username);

        if (optionalAlbumReview.isEmpty()){
            throw new AlbumReviewNotFoundException();
        }

        AlbumReview albumReview = optionalAlbumReview.get();

        albumReview.setRating(request.rating());
        albumReview.setReviewText(request.reviewText());

        albumReviewRepository.save(albumReview);

        return AlbumReviewResponse.fromAlbumReview(albumReview);
    }

    public void deleteAlbumReview(Integer id, String username) {
        Optional<AlbumReview> optionalAlbumReview = albumReviewRepository.findByIdAndUser_Username(id, username);

        if(optionalAlbumReview.isEmpty()){
            throw new AlbumReviewNotFoundException();
        }

        albumReviewRepository.deleteById(id);
    }


    private Album buildAlbum(AlbumReviewRequest request) throws URISyntaxException, IOException, InterruptedException {
        String artistName = request.artist();

        LastFMArtistInfo lastFMArtistInfo = lastFMService.getArtistInfo(request.artist());

        Artist artist = artistRepository
                .findByLastFMUrl(lastFMArtistInfo.getUrl())
                .orElseGet(() ->
                        createArtist(artistName, lastFMArtistInfo));

        Album album = new Album();

        album.setTitle(request.title());
        album.setArtist(artist);
        album.setMusicBrainzId(request.musicBrainzId());
        album.setLastFMUrl(request.lastFMUrl());


        LastFMAlbumInfo albumInfo = lastFMService.getAlbumInfo(request);

        String imageUrl = albumInfo
                .getImages()
                .getLast()
                .getImageUrl();

        album.setImageUrl(imageUrl);

        return album;
    }

    private AlbumReview buildAlbumReview(AlbumReviewRequest request, Album album, User user) {
        AlbumReview albumReview = new AlbumReview();
        albumReview.setAlbum(album);
        albumReview.setUser(user);
        albumReview.setRating(request.rating());
        albumReview.setReviewText(request.reviewText());
        albumReview.setCreated(Instant.now());

        return albumReview;
    }

    private Artist createArtist(String artistName, LastFMArtistInfo lastFMArtistInfo) {
        Artist artist = new Artist();

        artist.setName(artistName);
        artist.setLastFMUrl(lastFMArtistInfo.getUrl());
        artist.setMusicBrainzId(lastFMArtistInfo.getMbid());

        String imageUrl = null;

        if (lastFMArtistInfo.getMbid() != null &&
                !lastFMArtistInfo.getMbid().isBlank()) {

            try {
                imageUrl = fanartService.getFanartImageUrl(
                        lastFMArtistInfo.getMbid()
                );
            } catch (Exception e) {
                imageUrl = null;
            }
        }

        artist.setImageUrl(imageUrl);

        return artistRepository.save(artist);
    }
}
