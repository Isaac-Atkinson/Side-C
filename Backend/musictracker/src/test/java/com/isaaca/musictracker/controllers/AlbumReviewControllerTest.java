package com.isaaca.musictracker.controllers;

import com.isaaca.musictracker.constants.ReviewLimits;
import com.isaaca.musictracker.dto.request.AlbumReviewRequest;
import com.isaaca.musictracker.dto.request.UpdateAlbumReviewRequest;
import com.isaaca.musictracker.dto.response.review.AlbumReviewResponse;
import com.isaaca.musictracker.entities.Album;
import com.isaaca.musictracker.exceptions.AlbumAlreadyReviewedException;
import com.isaaca.musictracker.exceptions.AlbumReviewNotFoundException;
import com.isaaca.musictracker.services.AlbumService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;


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


    @Test
    void getAllAlbumReviewsReturnsAllReviews() throws Exception {
        AlbumReviewResponse review1 = new AlbumReviewResponse(
                1,
                10,
                new Album(),
                "great album"
        );

        AlbumReviewResponse review2 = new AlbumReviewResponse(
                2,
                10,
                new Album(),
                "Fantastic"
        );

        when(albumService.getAllAlbumReviewsByUser("username"))
                .thenReturn(List.of(review1, review2));

        mockMvc.perform(get("/api/albums/reviews")
                .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }
    @Test
    void getAlbumReviewByIdReturnsReview() throws Exception {

        AlbumReviewResponse review = new AlbumReviewResponse(
                1,
                10,
                new Album(),
                "great album"
        );



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
        AlbumReviewResponse review = new AlbumReviewResponse(
                1,
                10,
                new Album(),
                "great album"
        );

        AlbumReviewRequest reviewRequest = new AlbumReviewRequest(
                "1",
                "Melodrama",
                "Lorde",
                10,
                "great album"
        );

        when(albumService.addAlbumreview(reviewRequest, "username"))
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
        AlbumReviewRequest reviewRequest = new AlbumReviewRequest(
                "1",
                "Melodrama",
                "Lorde",
                10,
                "great album"
        );

        doThrow(new AlbumAlreadyReviewedException())
                .when(albumService)
                .addAlbumreview(reviewRequest, "username");

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
                "1",
                "",
                "Lorde",
                10,
                "great album"
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
                "1",
                "Melodrama",
                "",
                10,
                "great album"
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
                "1",
                "Melodrama",
                "Lorde",
                null,
                "great album"
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
                "1",
                "Melodrama",
                "Lorde",
                ReviewLimits.MAX_RATING + 1,
                "great album"
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
                "1",
                "Melodrama",
                "Lorde",
                ReviewLimits.MIN_RATING - 1,
                "great album"
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
                "1",
                "Melodrama",
                "Lorde",
                0,
                "a".repeat(ReviewLimits.MAX_REVIEW_LENGTH + 1)
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
        AlbumReviewResponse review = new AlbumReviewResponse(
                1,
                5,
                new Album(),
                "Actually not so great"
        );

        UpdateAlbumReviewRequest updateRequest = new UpdateAlbumReviewRequest(
                5,
                "Actually not so great"
        );

        when(albumService.updateAlbumReview(1, updateRequest, "username"))
            .thenReturn(review);

        mockMvc.perform(put("/api/albums/reviews/1")
                .principal(authentication)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.reviewText").value("Actually not so great"));
    }

    @Test
    void updateAlbumReviewReturnsNotFoundIfReviewDoesNotExist() throws Exception {
        UpdateAlbumReviewRequest updateRequest = new UpdateAlbumReviewRequest(
                5,
                "Actually not so great"
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
                "Great album"

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
                "Great album"
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
                "Great album"
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
                "a".repeat(ReviewLimits.MAX_REVIEW_LENGTH + 1)
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
                .deleteAlbumReviewById(1, "username");
    }

    @Test
    void deleteAlbumReviewReturnsNotFoundIfReviewDoesNotExist() throws Exception {
        doThrow(new AlbumReviewNotFoundException())
                .when(albumService)
                .deleteAlbumReviewById(1, "username");

        mockMvc.perform(delete("/api/albums/reviews/1")
                        .principal(authentication))
                .andExpect(status().isNotFound());
    }
}