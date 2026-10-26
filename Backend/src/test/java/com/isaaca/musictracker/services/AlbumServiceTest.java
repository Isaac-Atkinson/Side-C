package com.isaaca.musictracker.services;

import com.isaaca.musictracker.dto.entities.AlbumDTO;
import com.isaaca.musictracker.dto.entities.ArtistDTO;
import com.isaaca.musictracker.dto.request.AlbumReviewRequest;
import com.isaaca.musictracker.dto.request.UpdateAlbumReviewRequest;
import com.isaaca.musictracker.dto.response.review.AlbumReviewResponse;
import com.isaaca.musictracker.entities.Album;
import com.isaaca.musictracker.entities.AlbumReview;
import com.isaaca.musictracker.entities.Artist;
import com.isaaca.musictracker.entities.User;
import com.isaaca.musictracker.exceptions.AlbumAlreadyReviewedException;
import com.isaaca.musictracker.exceptions.AlbumReviewNotFoundException;
import com.isaaca.musictracker.externalapi.lastfm.albuminfo.LastFMAlbumInfo;
import com.isaaca.musictracker.externalapi.lastfm.albumsearch.LastFMImage;
import com.isaaca.musictracker.externalapi.lastfm.artistinfo.LastFMArtistInfo;
import com.isaaca.musictracker.repositories.AlbumRepository;
import com.isaaca.musictracker.repositories.AlbumReviewRepository;
import com.isaaca.musictracker.repositories.ArtistRepository;
import com.isaaca.musictracker.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlbumServiceTest {

    @Mock
    private FanartService fanartService;

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private AlbumReviewRepository albumReviewRepository;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private LastFMService lastFMService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AlbumService albumService;

    private User user;
    private AlbumReviewRequest request;
    private Album album;
    private AlbumDTO albumDTO;
    private int id;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("username");

        request = new AlbumReviewRequest(
                "1",
                "url",
                "Blonde",
                "Frank Ocean",
                10,
                "great",
                "2026-10-23"
        );

        id = 1;

        Artist artist = new Artist();
        artist.setName(request.artist());
        artist.setImageUrl("artist-image");

        album = new Album();
        album.setId(id);
        album.setTitle(request.title());
        album.setArtist(artist);
        album.setLastFMUrl(request.lastFMUrl());
        album.setMusicBrainzId(request.musicBrainzId());
        album.setImageUrl("album-image");

        albumDTO = new AlbumDTO(
                id,
                request.title(),
                new ArtistDTO(null, request.artist(), "artist-image"),
                "album-image"
        );
    }

    @Test
    void getAllAlbumReviewsByUserReturnsAllReviewsOfUser() {
        AlbumReview review1 = new AlbumReview();
        review1.setId(1);
        review1.setRating(10);
        review1.setReviewText("Great");
        review1.setAlbum(album);

        AlbumReview review2 = new AlbumReview();
        review2.setId(2);
        review2.setRating(7);
        review2.setReviewText("Pretty good");
        review2.setAlbum(album);

        when(albumReviewRepository.findByUser_Username(user.getUsername()))
                .thenReturn(List.of(review1, review2));

        List<AlbumReviewResponse> responses = albumService.getAllAlbumReviewsByUser(user.getUsername());

        assertEquals(2, responses.size());

        assertEquals(review1.getId(), responses.get(0).id());
        assertEquals(review1.getRating(), responses.get(0).rating());
        assertEquals(review1.getReviewText(), responses.get(0).reviewText());
        assertEquals(albumDTO, responses.get(0).album());

        assertEquals(review2.getId(), responses.get(1).id());
        assertEquals(review2.getRating(), responses.get(1).rating());
        assertEquals(review2.getReviewText(), responses.get(1).reviewText());
        assertEquals(albumDTO, responses.get(1).album());
    }

    @Test
    void getAlbumReviewByIdReturnsReviewWhenReviewExists() {
        AlbumReview review = new AlbumReview();
        review.setId(id);
        review.setRating(10);
        review.setReviewText("great");
        review.setAlbum(album);


        when(albumReviewRepository.findByIdAndUser_Username(id, user.getUsername()))
                .thenReturn(Optional.of(review));

        Optional<AlbumReviewResponse> response = albumService.getAlbumReviewById(id, user.getUsername());

        assertTrue(response.isPresent());

        assertEquals(review.getId(), response.get().id());
        assertEquals(review.getReviewText(), response.get().reviewText() );
        assertEquals(review.getRating(), response.get().rating());
        assertEquals(albumDTO, response.get().album());
    }

    @Test
    void getAlbumReviewByIdReturnsEmptyWhenReviewDoesNotExist() {
        when(albumReviewRepository.findByIdAndUser_Username(id, user.getUsername()))
                .thenReturn(Optional.empty());

        Optional<AlbumReviewResponse> response = albumService.getAlbumReviewById(id, user.getUsername());

        assertTrue(response.isEmpty());
    }

    @Test
    void addAlbumReviewSavesAlbumReviewWhenAlbumAlreadyExists() throws URISyntaxException, IOException, InterruptedException {

        when(userRepository.findUserByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));

        when(albumRepository.findByLastFMUrl(album.getLastFMUrl()))
                .thenReturn(Optional.of(album));

        AlbumReviewResponse response = albumService.addAlbumReview(request, user.getUsername());

        assertEquals(albumDTO, response.album());

        verify(albumRepository, never()).save(any());

        ArgumentCaptor<AlbumReview> captor =
                ArgumentCaptor.forClass(AlbumReview.class);

        verify(albumReviewRepository).save(captor.capture());

        AlbumReview savedReview = captor.getValue();

        assertEquals(request.rating(), savedReview.getRating());
        assertEquals(request.reviewText(), savedReview.getReviewText());
        assertEquals(album, savedReview.getAlbum());
        assertEquals(user, savedReview.getUser());
    }

    @Test
    void addAlbumReviewSavesAlbumReviewWhenAlbumDoesNotAlreadyExist() throws URISyntaxException, IOException, InterruptedException {

        when(userRepository.findUserByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));

        when(albumRepository.findByLastFMUrl(request.lastFMUrl()))
                .thenReturn(Optional.empty());

        LastFMAlbumInfo albumInfo = new LastFMAlbumInfo();
        LastFMImage image = new LastFMImage();
        image.setImageUrl("img");
        albumInfo.setImages(List.of(image));

        LastFMArtistInfo artistInfo = new LastFMArtistInfo();

        when(lastFMService.getAlbumInfo(request))
                .thenReturn(albumInfo);

        when(lastFMService.getArtistInfo(request.artist()))
                .thenReturn(artistInfo);

        when(artistRepository.save(any(Artist.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AlbumReviewResponse response = albumService.addAlbumReview(request, user.getUsername());

        assertEquals(new AlbumDTO(
                null,
                request.title(),
                new ArtistDTO(null, request.artist(), null),
                "img"
        ), response.album());

        verify(albumRepository).save(any(Album.class));

        ArgumentCaptor<AlbumReview> captor =
                ArgumentCaptor.forClass(AlbumReview.class);

        verify(albumReviewRepository).save(captor.capture());


        AlbumReview savedReview = captor.getValue();

        assertEquals(request.rating(), savedReview.getRating());
        assertEquals(request.reviewText(), savedReview.getReviewText());
        assertEquals(request.title(), savedReview.getAlbum().getTitle());
        assertEquals(request.artist(), savedReview.getAlbum().getArtist().getName());
        assertEquals(user, savedReview.getUser());
    }

    @Test
    void addAlbumReviewThrowsIfReviewAlreadyExists() throws URISyntaxException, IOException, InterruptedException {

        when(userRepository.findUserByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));

        when(albumReviewRepository.findByUser_UsernameAndAlbum_LastFMUrl
                (user.getUsername(), request.lastFMUrl()))
                .thenReturn(Optional.of(new AlbumReview()));

        assertThrows(AlbumAlreadyReviewedException.class,
                () -> albumService.addAlbumReview(request, user.getUsername()));

        verify(albumReviewRepository, never()).save(any());
    }

    @Test
    void updateAlbumReviewChangesReviewValues(){
        AlbumReview existingReview = new AlbumReview();
        existingReview.setRating(10);
        existingReview.setReviewText("Great");
        existingReview.setAlbum(album);

        UpdateAlbumReviewRequest updateRequest = new UpdateAlbumReviewRequest(
                5,
                "Not so great",
                "2026-09-23"
        );


        when(albumReviewRepository.findByIdAndUser_Username(id, user.getUsername() ))
                .thenReturn(Optional.of(existingReview));

        AlbumReviewResponse response = albumService.updateAlbumReview(id, updateRequest, user.getUsername());

        assertEquals(albumDTO, response.album());

        ArgumentCaptor<AlbumReview> captor =
                ArgumentCaptor.forClass(AlbumReview.class);

        verify(albumReviewRepository)
                .save(captor.capture());

        AlbumReview savedReview = captor.getValue();

        assertEquals(updateRequest.rating(), savedReview.getRating());
        assertEquals(updateRequest.reviewText(), savedReview.getReviewText());
        assertEquals(updateRequest.listenedOn(), savedReview.getListenedOn().toString());
    }

    @Test
    void updateAlbumReviewThrowsIfReviewDoesNotExist() {
        UpdateAlbumReviewRequest updateRequest = new UpdateAlbumReviewRequest(
                5,
                "Not so great",
                "2026-09-23"
        );


        when(albumReviewRepository.findByIdAndUser_Username(id, user.getUsername()))
                .thenReturn(Optional.empty());

        assertThrows(AlbumReviewNotFoundException.class,
                () -> albumService.updateAlbumReview(id, updateRequest, user.getUsername()));

        verify(albumReviewRepository, never()).save(any());
    }

    @Test
    void deleteAlbumReviewDeletesReviewIfItExists() {
        AlbumReview review = new AlbumReview();

        when(albumReviewRepository.findByIdAndUser_Username(id, user.getUsername()))
                .thenReturn(Optional.of(review));

        albumService.deleteAlbumReview(id, user.getUsername());

        verify(albumReviewRepository).deleteById(id);
    }

    @Test
    void deleteAlbumReviewThrowsIfReviewDoesNotExist() {

        when(albumReviewRepository.findByIdAndUser_Username(id, user.getUsername()))
                .thenReturn(Optional.empty());

        assertThrows(AlbumReviewNotFoundException.class,
                () -> albumService.deleteAlbumReview(id, user.getUsername()));

        verify(albumReviewRepository, never()).deleteById(any());
    }


}
