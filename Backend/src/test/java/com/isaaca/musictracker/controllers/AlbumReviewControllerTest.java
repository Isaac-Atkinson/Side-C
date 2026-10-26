package com.isaaca.musictracker.controllers;

import com.isaaca.musictracker.constants.ReviewLimits;
import com.isaaca.musictracker.dto.entities.AlbumDTO;
import com.isaaca.musictracker.dto.entities.ArtistDTO;
import com.isaaca.musictracker.dto.request.AlbumReviewRequest;
import com.isaaca.musictracker.dto.request.UpdateAlbumReviewRequest;
import com.isaaca.musictracker.dto.response.review.AlbumReviewResponse;
import com.isaaca.musictracker.dto.response.review.ArtistReviewResponse;
import com.isaaca.musictracker.entities.Album;
import com.isaaca.musictracker.entities.Artist;
import com.isaaca.musictracker.exceptions.AlbumAlreadyReviewedException;
import com.isaaca.musictracker.exceptions.AlbumReviewNotFoundException;
import com.isaaca.musictracker.services.AlbumService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;


import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AlbumReviewController.class)
class AlbumReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AlbumService albumService;

    @Autowired
    private ObjectMapper objectMapper;

    Authentication authentication =
            new UsernamePasswordAuthenticationToken(
                    "username",
                    null,
                    List.of()
            );

    Instant created = Instant.parse("2026-10-20T12:00:00Z");

    AlbumReviewResponse review;

    AlbumReviewRequest reviewRequest;



    @BeforeEach
    void setUp() {

        AlbumDTO album = new AlbumDTO(
                1,
                "Melodrama",
                ArtistDTO.from(new Artist()),
                "url"
        );

        review = new AlbumReviewResponse(
                1,
                10,
                album,
                "great album",
                created,
                LocalDate.parse("2026-10-23")
        );

        reviewRequest = new AlbumReviewRequest(
                "1",
                "url",
                "Melodrama",
                "Lorde",
                10,
                "great album",
                "2026-10-23"
        );
    }


    @Test
    void getAllAlbumReviewsReturnsAllReviews() throws Exception {

        AlbumDTO album2 = new AlbumDTO(
                2,
                "Discovery",
                ArtistDTO.from(new Artist()),
                "url"
        );

        AlbumReviewResponse review2 = new AlbumReviewResponse(
                2,
                10,
                album2,
                "Fantastic",
                created,
                LocalDate.parse("2026-10-23")
        );

        when(albumService.getAllAlbumReviewsByUser("username"))
                .thenReturn(List.of(review, review2));

        mockMvc.perform(get("/api/albums/reviews")
                .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }
    @Test
    void getAlbumReviewByIdReturnsReview() throws Exception {

        when(albumService.getAlbumReviewById(1, "username"))
                .thenReturn(Optional.of(review));

        mockMvc.perform(get("/api/albums/reviews/1")
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rating").value(10))
                .andExpect(jsonPath("$.reviewText").value("great album"));
    }

    @Test
    void getAlbumReviewByIdReturnsNotFoundIfReviewDoesNotExist() throws Exception {
        when(albumService.getAlbumReviewById(1, "username"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/albums/reviews/1")
                .principal(authentication))
                .andExpect(status().isNotFound());
    }

    @Test
    void addAlbumReviewReturnsCreatedReview() throws Exception {


        when(albumService.addAlbumReview(reviewRequest, "username"))
                .thenReturn(review);

        mockMvc.perform(post("/api/albums/reviews")
                .principal(authentication)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewRequest))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rating").value(10))
                .andExpect(jsonPath("$.reviewText").value("great album"));
    }

    @Test
    void addAlbumReviewReturnsConflictForDuplicateReview() throws Exception {


        doThrow(new AlbumAlreadyReviewedException())
                .when(albumService)
                .addAlbumReview(reviewRequest, "username");

        mockMvc.perform(post("/api/albums/reviews")
                .principal(authentication)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewRequest))
        )
                .andExpect(status().isConflict());
    }

    @Test
    void addAlbumReviewReturnsBadRequestForMissingTitle() throws Exception {
        AlbumReviewRequest invalidRequest = new AlbumReviewRequest(
                reviewRequest.musicBrainzId(),
                reviewRequest.lastFMUrl(),
                "",
                reviewRequest.artist(),
                reviewRequest.rating(),
                reviewRequest.reviewText(),
                reviewRequest.listenedOn()
        );

        mockMvc.perform(post("/api/albums/reviews")
                .principal(authentication)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest))
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    void addAlbumReviewReturnsBadRequestForMissingArtist() throws Exception {
        AlbumReviewRequest invalidRequest = new AlbumReviewRequest(
                reviewRequest.musicBrainzId(),
                reviewRequest.lastFMUrl(),
                reviewRequest.title(),
                "",
                reviewRequest.rating(),
                reviewRequest.reviewText(),
                reviewRequest.listenedOn()
        );

        mockMvc.perform(post("/api/albums/reviews")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void addAlbumReviewReturnsBadRequestForMissingLastFMUrl() throws Exception {
        AlbumReviewRequest invalidRequest = new AlbumReviewRequest(
                reviewRequest.musicBrainzId(),
                null,
                reviewRequest.title(),
                reviewRequest.artist(),
                reviewRequest.rating(),
                reviewRequest.reviewText(),
                reviewRequest.listenedOn()
        );

        mockMvc.perform(post("/api/albums/reviews")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void addAlbumReviewReturnsBadRequestForMissingRating() throws Exception {
        AlbumReviewRequest invalidRequest = new AlbumReviewRequest(
                reviewRequest.musicBrainzId(),
                reviewRequest.lastFMUrl(),
                reviewRequest.title(),
                reviewRequest.artist(),
                null,
                reviewRequest.reviewText(),
                reviewRequest.listenedOn()
        );

        mockMvc.perform(post("/api/albums/reviews")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void addAlbumReviewReturnsBadRequestForMissingListenedOn() throws Exception {
        AlbumReviewRequest invalidRequest = new AlbumReviewRequest(
                reviewRequest.musicBrainzId(),
                reviewRequest.lastFMUrl(),
                reviewRequest.title(),
                reviewRequest.artist(),
                reviewRequest.rating(),
                reviewRequest.reviewText(),
                ""
        );

        mockMvc.perform(post("/api/albums/reviews")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void addAlbumReviewReturnsBadRequestForRatingOverLimit() throws Exception {
        AlbumReviewRequest invalidRequest = new AlbumReviewRequest(
                reviewRequest.musicBrainzId(),
                reviewRequest.lastFMUrl(),
                reviewRequest.title(),
                reviewRequest.artist(),
                ReviewLimits.MAX_RATING + 1,
                reviewRequest.reviewText(),
                reviewRequest.listenedOn()
        );

        mockMvc.perform(post("/api/albums/reviews")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void addAlbumReviewReturnsBadRequestForRatingBelowLimit() throws Exception {
        AlbumReviewRequest invalidRequest = new AlbumReviewRequest(
                reviewRequest.musicBrainzId(),
                reviewRequest.lastFMUrl(),
                reviewRequest.title(),
                reviewRequest.artist(),
                ReviewLimits.MIN_RATING - 1,
                reviewRequest.reviewText(),
                reviewRequest.listenedOn()
        );

        mockMvc.perform(post("/api/albums/reviews")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void addAlbumReviewReturnsBadRequestForReviewTextLengthOverLimit() throws Exception {
        AlbumReviewRequest invalidRequest = new AlbumReviewRequest(
                reviewRequest.musicBrainzId(),
                reviewRequest.lastFMUrl(),
                reviewRequest.title(),
                reviewRequest.artist(),
                reviewRequest.rating(),
                "a".repeat(ReviewLimits.MAX_REVIEW_LENGTH + 1),
                reviewRequest.listenedOn()
        );

        mockMvc.perform(post("/api/albums/reviews")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateAlbumReviewReturnsUpdatedReview() throws Exception {

        AlbumReviewResponse updatedReview = new AlbumReviewResponse(
                1,
                5,
                new AlbumDTO(
                        1,
                        "Melodrama",
                        ArtistDTO.from(new Artist()),
                        "url"
                ),
                "Actually not so great",
                created,
                LocalDate.parse("2026-09-23")
        );

        UpdateAlbumReviewRequest updateRequest = new UpdateAlbumReviewRequest(
                5,
                "Actually not so great",
                "2026-09-23"
        );

        when(albumService.updateAlbumReview(1, updateRequest, "username"))
            .thenReturn(updatedReview);

        mockMvc.perform(put("/api/albums/reviews/1")
                .principal(authentication)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.reviewText").value("Actually not so great"))
                .andExpect(jsonPath("$.listenedOn").value("2026-09-23"));

    }

    @Test
    void updateAlbumReviewReturnsNotFoundIfReviewDoesNotExist() throws Exception {
        UpdateAlbumReviewRequest updateRequest = new UpdateAlbumReviewRequest(
                5,
                "Actually not so great",
                "2026-09-23"
        );

        doThrow(new AlbumReviewNotFoundException())
                .when(albumService)
                .updateAlbumReview(1, updateRequest, "username");

        mockMvc.perform(put("/api/albums/reviews/1")
                .principal(authentication)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
        )
                .andExpect(status().isNotFound());
    }

    @Test
    void updateAlbumReviewReturnsBadRequestForMissingRating() throws Exception {
        UpdateAlbumReviewRequest invalidRequest = new UpdateAlbumReviewRequest(
                null,
                "Great album",
                "2026-09-23"

        );

        mockMvc.perform(put("/api/albums/reviews/1")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateAlbumReviewReturnsBadRequestForMissingListenedOn() throws Exception {
        UpdateAlbumReviewRequest invalidRequest = new UpdateAlbumReviewRequest(
                10,
                "Great album",
                ""

        );

        mockMvc.perform(put("/api/albums/reviews/1")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateAlbumReviewReturnsBadRequestForRatingAboveLimit() throws Exception {
        UpdateAlbumReviewRequest invalidRequest = new UpdateAlbumReviewRequest(
                ReviewLimits.MAX_RATING + 1,
                "Great album",
                "2026-09-23"
        );

        mockMvc.perform(put("/api/albums/reviews/1")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateAlbumReviewReturnsBadRequestForRatingBelowLimit() throws Exception {
        UpdateAlbumReviewRequest invalidRequest = new UpdateAlbumReviewRequest(
                ReviewLimits.MIN_RATING - 1,
                "Great album",
                "2026-09-23"
        );

        mockMvc.perform(put("/api/albums/reviews/1")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateAlbumReviewReturnsBadRequestForReviewTextLengthOverLimit() throws Exception {
        UpdateAlbumReviewRequest invalidRequest = new UpdateAlbumReviewRequest(
                0,
                "a".repeat(ReviewLimits.MAX_REVIEW_LENGTH + 1),
                "2026-09-23"
        );

        mockMvc.perform(put("/api/albums/reviews/1")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteAlbumReviewDeletesReview() throws Exception {
        mockMvc.perform(delete("/api/albums/reviews/1")
                .principal(authentication)
        )
                .andExpect(status().isNoContent());

        verify(albumService)
                .deleteAlbumReview(1, "username");
    }

    @Test
    void deleteAlbumReviewReturnsNotFoundIfReviewDoesNotExist() throws Exception {
        doThrow(new AlbumReviewNotFoundException())
                .when(albumService)
                .deleteAlbumReview(1, "username");

        mockMvc.perform(delete("/api/albums/reviews/1")
                        .principal(authentication))
                .andExpect(status().isNotFound());
    }
}