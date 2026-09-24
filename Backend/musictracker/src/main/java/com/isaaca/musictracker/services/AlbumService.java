package com.isaaca.musictracker.services;

import com.isaaca.musictracker.entities.Album;
import com.isaaca.musictracker.entities.AlbumReview;
import com.isaaca.musictracker.entities.Artist;
import com.isaaca.musictracker.entities.User;
import com.isaaca.musictracker.exceptions.AlbumAlreadyReviewedException;
import com.isaaca.musictracker.exceptions.AlbumReviewNotFoundException;
import com.isaaca.musictracker.externalapi.lastfm.albuminfo.LastFMAlbumInfo;
import com.isaaca.musictracker.externalapi.lastfm.artistinfo.LastFMArtistInfo;
import com.isaaca.musictracker.externalapi.lastfm.artistsearch.LastFMArtist;
import com.isaaca.musictracker.repositories.AlbumRepository;
import com.isaaca.musictracker.repositories.AlbumReviewRepository;
import com.isaaca.musictracker.repositories.ArtistRepository;
import com.isaaca.musictracker.repositories.UserRepository;
import com.isaaca.musictracker.requests.AlbumRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Service
public class AlbumService {

    private final UserRepository userRepository;
    @Value("${API_KEY}")
    private String API_KEY;

    @Value("${lastfm.api.root}")
    private String lastFMApiRoot;

    private final AlbumRepository albumRepository;
    private final AlbumReviewRepository albumReviewRepository;
    private final ArtistRepository artistRepository;
    private final LastFMService lastFMService;

    public AlbumService(
            AlbumRepository albumRepository,
            AlbumReviewRepository albumReviewRepository,
            ArtistRepository artistRepository,
            LastFMService lastFMService,
            UserRepository userRepository) {
        this.albumRepository = albumRepository;
        this.albumReviewRepository = albumReviewRepository;
        this.artistRepository = artistRepository;
        this.lastFMService = lastFMService;
        this.userRepository = userRepository;
    }

    public List<AlbumReview> getAllAlbumReviewsByUser(String username)  {

        return albumReviewRepository.findByUser_Username(username);
    }

    public Optional<AlbumReview> getAlbumReviewById(Integer id, String username)  {
        return albumReviewRepository.findByIdAndUser_Username(id, username);
    }

    @Transactional
    public AlbumReview addAlbumreview(AlbumRequest albumRequest, String username)
            throws URISyntaxException, IOException, InterruptedException {

        User user = userRepository
                .findUserByUsername(username)
                .orElseThrow();

        String uniqueId = "";
        if(albumRequest.getMusicBrainzId() == null ||
                albumRequest.getMusicBrainzId().isBlank()){
            uniqueId = albumRequest.getTitle() + albumRequest.getArtist();
        } else {
            uniqueId = albumRequest.getMusicBrainzId();
        }

        Optional<AlbumReview> optionalAlbumReview = albumReviewRepository
                .findByUser_UsernameAndAlbum_UniqueIdentifier(
                        username,
                        uniqueId);

        if(optionalAlbumReview.isPresent()) {
            throw new AlbumAlreadyReviewedException();
        }

        Optional<Album> optionalAlbum = albumRepository.findByUniqueIdentifier(uniqueId);

        if (optionalAlbum.isEmpty()){
            Album album = buildAlbum(albumRequest, uniqueId);
            albumRepository.save(album);

            AlbumReview albumReview = buildAlbumReview(albumRequest, album, user);

            return albumReviewRepository.save(albumReview);
        }

        Album album = optionalAlbum.get();

        AlbumReview albumReview = buildAlbumReview(albumRequest, album, user);

        return albumReviewRepository.save(albumReview);
    }

    public AlbumReview updateAlbumReview(Integer id, AlbumRequest albumRequest, String username) {
        Optional<AlbumReview> optionalAlbumReview = albumReviewRepository.findByIdAndUser_Username(id, username);

        if (optionalAlbumReview.isEmpty()){
            throw new AlbumReviewNotFoundException();
        }

        AlbumReview albumReview = optionalAlbumReview.get();

        albumReview.setRating(albumRequest.getRating());
        albumReview.setReviewText(albumRequest.getReviewText());

        return albumReviewRepository.save(albumReview);
    }

    public void deleteAlbumReviewById(Integer id, String username) {
        Optional<AlbumReview> optionalAlbumReview = albumReviewRepository.findByIdAndUser_Username(id, username);

        if(optionalAlbumReview.isPresent()){
            albumReviewRepository.deleteById(id);
        } else throw new AlbumReviewNotFoundException();
    }

    private Album buildAlbum(AlbumRequest albumRequest, String uniqueId) throws URISyntaxException, IOException, InterruptedException {
        String artistName = albumRequest.getArtist();

        LastFMArtistInfo lastFMArtistInfo = lastFMService.getArtistInfo(albumRequest.getArtist());

        Artist artist = artistRepository
                .findByLastFMUrl(lastFMArtistInfo.getUrl())
                .orElseGet(() ->
                        createArtist(artistName, lastFMArtistInfo));

        Album album = new Album();

        album.setTitle(albumRequest.getTitle());
        album.setArtist(artist);
        album.setMusicBrainzId(albumRequest.getMusicBrainzId());
        album.setUniqueIdentifier(uniqueId);


        LastFMAlbumInfo albumInfo = lastFMService.getAlbumInfo(albumRequest);

        String imageUrl = albumInfo
                .getImages()
                .getLast()
                .getImageUrl();

        album.setImageUrl(imageUrl);

        return album;
    }

    private AlbumReview buildAlbumReview(AlbumRequest albumRequest, Album album, User user) {
        AlbumReview albumReview = new AlbumReview();
        albumReview.setAlbum(album);
        albumReview.setUser(user);
        albumReview.setRating(albumRequest.getRating());
        albumReview.setReviewText(albumRequest.getReviewText());

        return albumReview;
    }

    private Artist createArtist(String artistName, LastFMArtistInfo lastFMArtistInfo) {
        Artist artist = new Artist();

        artist.setName(artistName);
        artist.setLastFMUrl(lastFMArtistInfo.getUrl());
        artist.setMusicBrainzId(lastFMArtistInfo.getMbid());

        return artistRepository.save(artist);
    }
}
