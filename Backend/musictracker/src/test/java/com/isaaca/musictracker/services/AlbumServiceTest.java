package com.isaaca.musictracker.services;

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
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
                "great"
        );

        id = 1;
    }

    @Test
    void getAllAlbumReviewsByUserReturnsAllReviewsOfUser() {
        AlbumReview review1 = new AlbumReview();
        review1.setId(1);
        review1.setRating(10);
        review1.setReviewText("Great");
        review1.setAlbum(new Album());

        AlbumReview review2 = new AlbumReview();
        review2.setId(2);
        review2.setRating(7);
        review2.setReviewText("Pretty good");
        review2.setAlbum(new Album());

        when(albumReviewRepository.findByUser_Username(user.getUsername()))
                .thenReturn(List.of(review1, review2));

        List<AlbumReviewResponse> responses = albumService.getAllAlbumReviewsByUser(user.getUsername());

        assertEquals(2, responses.size());

        assertEquals(review1.getId(), responses.get(0).id());
        assertEquals(review1.getRating(), responses.get(0).rating());
        assertEquals(review1.getReviewText(), responses.get(0).reviewText());

        assertEquals(review2.getId(), responses.get(1).id());
        assertEquals(review2.getRating(), responses.get(1).rating());
        assertEquals(review2.getReviewText(), responses.get(1).reviewText());
    }

    @Test
    void getAlbumReviewByIdReturnsReviewWhenReviewExists() {
        AlbumReview review = new AlbumReview();
        review.setId(id);
        review.setRating(10);
        review.setReviewText("great");
        review.setAlbum(new Album());


        when(albumReviewRepository.findByIdAndUser_Username(id, user.getUsername()))
                .thenReturn(Optional.of(review));

        Optional<AlbumReviewResponse> response = albumService.getAlbumReviewById(id, user.getUsername());

        assertTrue(response.isPresent());

        assertEquals(review.getId(), response.get().id());
        assertEquals(review.getReviewText(), response.get().reviewText() );
        assertEquals(review.getRating(), response.get().rating());
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

        Album album = new Album();
        album.setLastFMUrl(request.lastFMUrl());
        when(albumRepository.findByLastFMUrl(album.getLastFMUrl()))
                .thenReturn(Optional.of(album));

        albumService.addAlbumReview(request, user.getUsername());

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

        albumService.addAlbumReview(request, user.getUsername());

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

        UpdateAlbumReviewRequest updateRequest = new UpdateAlbumReviewRequest(
                5,
                "Not so great"
        );


        when(albumReviewRepository.findByIdAndUser_Username(id, user.getUsername() ))
                .thenReturn(Optional.of(existingReview));

        albumService.updateAlbumReview(id, updateRequest,  user.getUsername());

        ArgumentCaptor<AlbumReview> captor =
                ArgumentCaptor.forClass(AlbumReview.class);

        verify(albumReviewRepository)
                .save(captor.capture());

        AlbumReview savedReview = captor.getValue();

        assertEquals(updateRequest.rating(), savedReview.getRating());
        assertEquals(updateRequest.reviewText(), savedReview.getReviewText());
    }

    @Test
    void updateAlbumReviewThrowsIfReviewDoesNotExist() {
        UpdateAlbumReviewRequest updateRequest = new UpdateAlbumReviewRequest(
                5,
                "Not so great"
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