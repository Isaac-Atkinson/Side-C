package com.isaaca.musictracker.services;

import com.isaaca.musictracker.dto.entities.ArtistDTO;
import com.isaaca.musictracker.dto.request.ArtistReviewRequest;
import com.isaaca.musictracker.dto.request.UpdateArtistReviewRequest;
import com.isaaca.musictracker.dto.response.review.ArtistReviewResponse;
import com.isaaca.musictracker.entities.Artist;
import com.isaaca.musictracker.entities.ArtistReview;
import com.isaaca.musictracker.entities.User;
import com.isaaca.musictracker.exceptions.ArtistAlreadyReviewedException;
import com.isaaca.musictracker.exceptions.ArtistReviewNotFoundException;
import com.isaaca.musictracker.repositories.ArtistRepository;
import com.isaaca.musictracker.repositories.ArtistReviewRepository;
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
class ArtistServiceTest {

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private LastFMService lastFMService;

    @Mock
    private FanartService fanartService;

    @Mock
    private ArtistReviewRepository artistReviewRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ArtistService artistService;

    private User user;
    private ArtistReviewRequest request;
    private Artist artist;
    private ArtistDTO artistDTO;
    private int id;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("username");

        request = new ArtistReviewRequest(
                "1",
                "url",
                "Lorde",
                "img",
                10,
                "great"
        );

        id = 1;

        artist = new Artist();
        artist.setName(request.name());
        artist.setMusicBrainzId(request.musicBrainzId());
        artist.setLastFMUrl(request.lastFMUrl());
        artist.setImageUrl(request.imageUrl());

        artistDTO = new ArtistDTO(null, request.name(), request.imageUrl());
    }

    @Test
    void getAllArtistReviewsByUserReturnsAllReviewsOfUser() {
        ArtistReview review1 = new ArtistReview();
        review1.setId(1);
        review1.setRating(10);
        review1.setReviewText("Great");
        review1.setArtist(artist);

        ArtistReview review2 = new ArtistReview();
        review2.setId(2);
        review2.setRating(7);
        review2.setReviewText("Pretty good");
        review2.setArtist(artist);

        when(artistReviewRepository.findByUser_Username(user.getUsername()))
                .thenReturn(List.of(review1, review2));

        List<ArtistReviewResponse> responses = artistService.getAllArtistReviewsByUser(user.getUsername());

        assertEquals(2, responses.size());

        assertEquals(review1.getId(), responses.get(0).id());
        assertEquals(review1.getRating(), responses.get(0).rating());
        assertEquals(review1.getReviewText(), responses.get(0).reviewText());
        assertEquals(artistDTO, responses.get(0).artist());

        assertEquals(review2.getId(), responses.get(1).id());
        assertEquals(review2.getRating(), responses.get(1).rating());
        assertEquals(review2.getReviewText(), responses.get(1).reviewText());
        assertEquals(artistDTO, responses.get(1).artist());
    }

    @Test
    void getArtistReviewByIdReturnsReviewWhenReviewExists() {
        ArtistReview review = new ArtistReview();
        review.setId(id);
        review.setRating(10);
        review.setReviewText("great");
        review.setArtist(artist);


        when(artistReviewRepository.findByIdAndUser_Username(id, user.getUsername()))
                .thenReturn(Optional.of(review));

        Optional<ArtistReviewResponse> response = artistService.getArtistReviewById(id, user.getUsername());

        assertTrue(response.isPresent());

        assertEquals(review.getId(), response.get().id());
        assertEquals(review.getReviewText(), response.get().reviewText() );
        assertEquals(review.getRating(), response.get().rating());
        assertEquals(artistDTO, response.get().artist());
    }

    @Test
    void getArtistReviewByIdReturnsEmptyWhenReviewDoesNotExist() {
        when(artistReviewRepository.findByIdAndUser_Username(id, user.getUsername()))
                .thenReturn(Optional.empty());

        Optional<ArtistReviewResponse> response = artistService.getArtistReviewById(id, user.getUsername());

        assertTrue(response.isEmpty());
    }

    @Test
    void addArtistReviewSavesArtistReviewWhenArtistAlreadyExists() throws URISyntaxException, IOException, InterruptedException {

        when(userRepository.findUserByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));

        when(artistRepository.findByLastFMUrl(artist.getLastFMUrl()))
                .thenReturn(Optional.of(artist));

        when(artistReviewRepository.save(any(ArtistReview.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ArtistReviewResponse response = artistService.addArtistReview(request, user.getUsername());

        assertEquals(artistDTO, response.artist());

        verify(artistRepository, never()).save(any());

        ArgumentCaptor<ArtistReview> captor =
                ArgumentCaptor.forClass(ArtistReview.class);

        verify(artistReviewRepository).save(captor.capture());

        ArtistReview savedReview = captor.getValue();

        assertEquals(request.rating(), savedReview.getRating());
        assertEquals(request.reviewText(), savedReview.getReviewText());
        assertEquals(artist, savedReview.getArtist());
        assertEquals(user, savedReview.getUser());
    }

    @Test
    void addArtistReviewSavesArtistReviewWhenArtistDoesNotAlreadyExist() throws URISyntaxException, IOException, InterruptedException {

        when(userRepository.findUserByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));

        when(artistRepository.findByLastFMUrl(request.lastFMUrl()))
                .thenReturn(Optional.empty());

        when(artistRepository.save(any(Artist.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(artistReviewRepository.save(any(ArtistReview.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ArtistReviewResponse response = artistService.addArtistReview(request, user.getUsername());

        assertEquals(artistDTO, response.artist());

        verify(artistRepository).save(any(Artist.class));

        ArgumentCaptor<ArtistReview> captor =
                ArgumentCaptor.forClass(ArtistReview.class);

        verify(artistReviewRepository).save(captor.capture());


        ArtistReview savedReview = captor.getValue();

        assertEquals(request.rating(), savedReview.getRating());
        assertEquals(request.reviewText(), savedReview.getReviewText());
        assertEquals(request.name(), savedReview.getArtist().getName());
        assertEquals(request.imageUrl(), savedReview.getArtist().getImageUrl());
        assertEquals(user, savedReview.getUser());
    }

    @Test
    void addArtistReviewThrowsIfReviewAlreadyExists() throws URISyntaxException, IOException, InterruptedException {

        when(userRepository.findUserByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));

        when(artistReviewRepository.findByUser_UsernameAndArtist_LastFMUrl
                (user.getUsername(), request.lastFMUrl()))
                .thenReturn(Optional.of(new ArtistReview()));

        assertThrows(ArtistAlreadyReviewedException.class,
                () -> artistService.addArtistReview(request, user.getUsername()));

        verify(artistReviewRepository, never()).save(any());
    }

    @Test
    void updateArtistReviewChangesReviewValues(){
        ArtistReview existingReview = new ArtistReview();
        existingReview.setRating(10);
        existingReview.setReviewText("Great");
        existingReview.setArtist(artist);

        UpdateArtistReviewRequest updateRequest = new UpdateArtistReviewRequest(
                5,
                "Not so great"
        );


        when(artistReviewRepository.findByIdAndUser_Username(id, user.getUsername() ))
                .thenReturn(Optional.of(existingReview));

        ArtistReviewResponse response = artistService.updateArtistReview(id, updateRequest, user.getUsername());

        assertEquals(artistDTO, response.artist());

        ArgumentCaptor<ArtistReview> captor =
                ArgumentCaptor.forClass(ArtistReview.class);

        verify(artistReviewRepository)
                .save(captor.capture());

        ArtistReview savedReview = captor.getValue();

        assertEquals(updateRequest.rating(), savedReview.getRating());
        assertEquals(updateRequest.reviewText(), savedReview.getReviewText());
    }

    @Test
    void updateArtistReviewThrowsIfReviewDoesNotExist() {
        UpdateArtistReviewRequest updateRequest = new UpdateArtistReviewRequest(
                5,
                "Not so great"
        );


        when(artistReviewRepository.findByIdAndUser_Username(id, user.getUsername()))
                .thenReturn(Optional.empty());

        assertThrows(ArtistReviewNotFoundException.class,
                () -> artistService.updateArtistReview(id, updateRequest, user.getUsername()));

        verify(artistReviewRepository, never()).save(any());
    }

    @Test
    void deleteArtistReviewDeletesReviewIfItExists() {
        ArtistReview review = new ArtistReview();

        when(artistReviewRepository.findByIdAndUser_Username(id, user.getUsername()))
                .thenReturn(Optional.of(review));

        artistService.deleteArtistReview(id, user.getUsername());

        verify(artistReviewRepository).deleteById(id);
    }

    @Test
    void deleteArtistReviewThrowsIfReviewDoesNotExist() {

        when(artistReviewRepository.findByIdAndUser_Username(id, user.getUsername()))
                .thenReturn(Optional.empty());

        assertThrows(ArtistReviewNotFoundException.class,
                () -> artistService.deleteArtistReview(id, user.getUsername()));

        verify(artistReviewRepository, never()).deleteById(any());
    }

}
