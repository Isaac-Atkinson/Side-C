package com.isaaca.musictracker.controllers;

import com.isaaca.musictracker.constants.ReviewLimits;
import com.isaaca.musictracker.dto.request.AlbumReviewRequest;
import com.isaaca.musictracker.dto.request.ArtistReviewRequest;
import com.isaaca.musictracker.dto.request.UpdateAlbumReviewRequest;
import com.isaaca.musictracker.dto.request.UpdateArtistReviewRequest;
import com.isaaca.musictracker.dto.response.review.AlbumReviewResponse;
import com.isaaca.musictracker.dto.response.review.ArtistReviewResponse;
import com.isaaca.musictracker.entities.Album;
import com.isaaca.musictracker.entities.Artist;
import com.isaaca.musictracker.exceptions.AlbumAlreadyReviewedException;
import com.isaaca.musictracker.exceptions.AlbumReviewNotFoundException;
import com.isaaca.musictracker.exceptions.ArtistAlreadyReviewedException;
import com.isaaca.musictracker.exceptions.ArtistReviewNotFoundException;
import com.isaaca.musictracker.services.AlbumService;
import com.isaaca.musictracker.services.ArtistService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ArtistReviewController.class)
class ArtistReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ArtistService artistService;

    @Autowired
    private ObjectMapper objectMapper;

    Authentication authentication =
            new UsernamePasswordAuthenticationToken(
                    "username",
                    null,
                    List.of()
            );

    @Test
    void getAllArtistReviewsReturnsAllReviews() throws Exception {
        ArtistReviewResponse review1 = new ArtistReviewResponse(
                1,
                10,
                new Artist(),
                "Generational"
        );

        ArtistReviewResponse review2 = new ArtistReviewResponse(
                2,
                10,
                new Artist(),
                "Fantastic"
        );

        when(artistService.getAllArtistReviews("username"))
                .thenReturn(List.of(review1, review2));

        mockMvc.perform(get("/api/artists/reviews")
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }
    @Test
    void getArtistReviewByIdReturnsReview() throws Exception {

        ArtistReviewResponse review = new ArtistReviewResponse(
                1,
                10,
                new Artist(),
                "Generational"
        );



        when(artistService.getArtistReviewById(1, "username"))
                .thenReturn(Optional.of(review));

        mockMvc.perform(get("/api/artists/reviews/1")
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rating").value(10))
                .andExpect(jsonPath("$.reviewText").value("Generational"));
    }

    @Test
    void getArtistReviewByIdReturnsNotFoundIfReviewDoesNotExist() throws Exception {
        when(artistService.getArtistReviewById(1, "username"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/artists/reviews/1")
                        .principal(authentication))
                .andExpect(status().isNotFound());
    }

    @Test
    void addArtistReviewReturnsCreatedReview() throws Exception {
        ArtistReviewResponse review = new ArtistReviewResponse(
                1,
                10,
                new Artist(),
                "Generational"
        );


        ArtistReviewRequest reviewRequest = new ArtistReviewRequest(
                "1",
                "Frank Ocean",
                "img",
                10,
                "Generational"
        );

        when(artistService.addArtistReview(reviewRequest, "username"))
                .thenReturn(review);

        mockMvc.perform(post("/api/artists/reviews")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rating").value(10))
                .andExpect(jsonPath("$.reviewText").value("Generational"));
    }

    @Test
    void addArtistReviewReturnsConflictForDuplicateReview() throws Exception {
        ArtistReviewRequest reviewRequest = new ArtistReviewRequest(
                "1",
                "Frank Ocean",
                "img",
                10,
                "Come back"
        );

        doThrow(new ArtistAlreadyReviewedException())
                .when(artistService)
                .addArtistReview(reviewRequest, "username");

        mockMvc.perform(post("/api/artists/reviews")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest))
                )
                .andExpect(status().isConflict());
    }

    @Test
    void addArtistReviewReturnsBadRequestForMissingName() throws Exception {
        ArtistReviewRequest invalidRequest = new ArtistReviewRequest(
                "1",
                "",
                "img",
                10,
                "Come back"
        );

        mockMvc.perform(post("/api/artists/reviews")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                )
                .andExpect(status().isBadRequest());
    }



    @Test
    void addArtistReviewReturnsBadRequestForMissingRating() throws Exception {
        ArtistReviewRequest invalidRequest = new ArtistReviewRequest(
                "1",
                "Frank Ocean",
                "img",
                null,
                "Come back"
        );

        mockMvc.perform(post("/api/artists/reviews")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void addArtistReviewReturnsBadRequestForRatingOverLimit() throws Exception {
        ArtistReviewRequest invalidRequest = new ArtistReviewRequest(
                "1",
                "Frank Ocean",
                "img",
                ReviewLimits.MAX_RATING + 1,
                "Come back"
        );

        mockMvc.perform(post("/api/artists/reviews")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void addArtistReviewReturnsBadRequestForRatingBelowLimit() throws Exception {
        ArtistReviewRequest invalidRequest = new ArtistReviewRequest(
                "1",
                "Frank Ocean",
                "img",
                ReviewLimits.MIN_RATING - 1,
                "Come back"
        );

        mockMvc.perform(post("/api/artists/reviews")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void addArtistReviewReturnsBadRequestForReviewTextLengthOverLimit() throws Exception {
        ArtistReviewRequest invalidRequest = new ArtistReviewRequest(
                "1",
                "Frank Ocean",
                "img",
                10,
                "a".repeat(ReviewLimits.MAX_REVIEW_LENGTH + 1)
        );

        mockMvc.perform(post("/api/artists/reviews")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateArtistReviewReturnsUpdatedReview() throws Exception {
        ArtistReviewResponse review = new ArtistReviewResponse(
                1,
                5,
                new Artist(),
                "Actually not so great"
        );

        UpdateArtistReviewRequest updateRequest = new UpdateArtistReviewRequest(
                5,
                "Actually not so great"
        );

        when(artistService.updateArtistReview(1, updateRequest, "username"))
                .thenReturn(review);

        mockMvc.perform(put("/api/artists/reviews/1")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.reviewText").value("Actually not so great"));
    }

    @Test
    void updateArtistReviewReturnsNotFoundIfReviewDoesNotExist() throws Exception {
        UpdateArtistReviewRequest updateRequest = new UpdateArtistReviewRequest(
                5,
                "Actually not so great"
        );

        doThrow(new ArtistReviewNotFoundException())
                .when(artistService)
                .updateArtistReview(1, updateRequest, "username");

        mockMvc.perform(put("/api/artists/reviews/1")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void updateArtistReviewReturnsBadRequestForMissingRating() throws Exception {
        UpdateArtistReviewRequest invalidRequest = new UpdateArtistReviewRequest(
                null,
                "Actually not so great"
        );

        mockMvc.perform(put("/api/artists/reviews/1")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateArtistReviewReturnsBadRequestForRatingAboveLimit() throws Exception {
        UpdateArtistReviewRequest invalidRequest = new UpdateArtistReviewRequest(
                ReviewLimits.MAX_RATING + 1,
                "Actually not so great"
        );

        mockMvc.perform(put("/api/artists/reviews/1")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateArtistReviewReturnsBadRequestForRatingBelowLimit() throws Exception {
        UpdateArtistReviewRequest invalidRequest = new UpdateArtistReviewRequest(
                ReviewLimits.MIN_RATING - 1,
                "Actually not so great"
        );

        mockMvc.perform(put("/api/artists/reviews/1")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateArtistReviewReturnsBadRequestForReviewTextLengthOverLimit() throws Exception {
        UpdateArtistReviewRequest invalidRequest = new UpdateArtistReviewRequest(
                10,
                "a".repeat(ReviewLimits.MAX_REVIEW_LENGTH + 1)
        );

        mockMvc.perform(put("/api/artists/reviews/1")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteArtistReviewDeletesReview() throws Exception {
        mockMvc.perform(delete("/api/artists/reviews/1")
                        .principal(authentication)
                )
                .andExpect(status().isNoContent());

        verify(artistService)
                .deleteArtistReviewById(1, "username");
    }

    @Test
    void deleteArtistReviewReturnsNotFoundIfReviewDoesNotExist() throws Exception {
        doThrow(new ArtistReviewNotFoundException())
                .when(artistService)
                .deleteArtistReviewById(1, "username");

        mockMvc.perform(delete("/api/artists/reviews/1")
                        .principal(authentication))
                .andExpect(status().isNotFound());
    }

}