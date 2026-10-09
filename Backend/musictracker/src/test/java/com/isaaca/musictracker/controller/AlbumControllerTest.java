package com.isaaca.musictracker.controller;


import com.isaaca.musictracker.entities.Album;
import com.isaaca.musictracker.entities.Artist;
import com.isaaca.musictracker.repositories.AlbumRepository;
import com.isaaca.musictracker.repositories.ArtistRepository;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(AlbumController.class)
public class AlbumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AlbumRepository albumRepository;

    @MockitoBean
    private ArtistRepository artistRepository;

    @Test
    void getAlbumByID_returnsAlbum_ifAlbumExists() throws Exception {
        final int id = 1;
        final String title = "Blonde";
        final String artistName = "Frank Ocean";
        final int year = 2016;
        final int rating = 10;

        Artist artist = new Artist();
        artist.setName(artistName);

        Album album = new Album();
        album.setId(id);
        album.setTitle(title);
        album.setArtist(artist);
        album.setYear(year);
        album.setRating(rating);

        when(albumRepository.findById(1))
                .thenReturn(Optional.of(album));

        final String uri = "/api/albums/" + Integer.toString(id);

        mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.artist.name").value(artistName))
                .andExpect(jsonPath("$.year").value(year))
                .andExpect(jsonPath("$.rating").value(rating));
    }

    @Test
    void getAlbumByID_returnsNoAlbum_ifAlbumDoesNotExist() throws Exception {
        final int id = 1;

        when(albumRepository.findById(id))
                .thenReturn(Optional.empty());

        final String uri = "/api/albums/" + Integer.toString(id);

        mockMvc.perform(get(uri))
                .andExpect(status().isNotFound());
    }

    @Test
    void canAddValidAlbum()  throws Exception {
        String requestBody = """
                {
                  "title": "Blonde",
                  "year": 2016,
                  "rating": 10,
                  "artist": "Frank Ocean"
                }
                """;

        mockMvc.perform(post("/api/albums")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    void cannotAddDuplicateAlbum()  throws Exception {
        String requestBody = """
                {
                  "title": "Blonde",
                  "year": 2016,
                  "rating": 10,
                  "artist": "Frank Ocean"
                }
                """;

        Album existingAlbum = new Album();
        existingAlbum.setTitle("Blonde");

        when(albumRepository
                .findByTitleIgnoreCaseAndArtist_NameIgnoreCase(
                        "Blonde",
                        "Frank Ocean"))
                .thenReturn(Optional.of(existingAlbum));

        mockMvc.perform(post("/api/albums")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict());

        verify(albumRepository, never()).save(any(Album.class));
    }

    @Test
    void cannotAddAlbumWithoutTitle()  throws Exception {
        String requestBody = """
                {
                  "year": 2016,
                  "rating": 10,
                  "artist": "Frank Ocean"
                }
                """;

        mockMvc.perform(post("/api/albums")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

    }

    @Test
    void cannotAddAlbumWithoutYear()  throws Exception {
        String requestBody = """
                {
                  "title": "Blonde",
                  "rating": 10,
                  "artist": "Frank Ocean"
                }
                """;

        mockMvc.perform(post("/api/albums")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void cannotAddAlbumWithoutRating()  throws Exception {
        String requestBody = """
                {
                  "title": "Blonde",
                  "year": 2016,
                  "artist": "Frank Ocean"
                }
                """;

        mockMvc.perform(post("/api/albums")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void cannotAddAlbumWithoutArtist()  throws Exception {
        String requestBody = """
                {
                  "title": "Blonde",
                  "year": 2016,
                  "rating": 10
                }
                """;

        mockMvc.perform(post("/api/albums")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void cannotAddAlbumWithRatingBelowZero()  throws Exception {
        String requestBody = """
                {
                  "title": "Blonde",
                  "year": 2016,
                  "rating": -1
                }
                """;

        mockMvc.perform(post("/api/albums")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void cannotAddAlbumWithRatingAboveTen()  throws Exception {
        String requestBody = """
                {
                  "title": "Blonde",
                  "year": 2016,
                  "rating": 15
                }
                """;

        mockMvc.perform(post("/api/albums")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void canUpdateAlbum()  throws Exception {
        final int id = 1;

        Album existingAlbum = new Album();

        Artist existingArtist = new Artist();
        existingArtist.setName("Frank Ocean");


        String requestBody = """
                {
                  "title":  "Blonde",
                  "year": 2016,
                  "rating": 10,
                  "artist": "Frank Ocean"
                }
                """;

        when(albumRepository.findById(id))
                .thenReturn(Optional.of(existingAlbum));

        when(artistRepository.findByNameIgnoreCase("Frank Ocean"))
                .thenReturn(Optional.of(existingArtist));

        final String uri = "/api/albums/" + Integer.toString(id);

        mockMvc.perform(put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());

        verify(albumRepository).save(existingAlbum);
    }


    @Test
    void cannotUpdateAlbum_IfAlbumDoesntExist()  throws Exception {
        final int id = 1;

        String requestBody = """
                {
                  "title":  "Blonde",
                  "year": 2016,
                  "rating": 10,
                  "artist": "Frank Ocean"
                }
                """;

        when(albumRepository.findById(id))
                .thenReturn(Optional.empty());

        final String uri = "/api/albums/" + Integer.toString(id);

        mockMvc.perform(put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    void canDeleteAlbum_IfExists()  throws Exception {
        final int id = 1;
        Album existingAlbum = new Album();

        Artist existingArtist = new Artist();
        existingArtist.setName("Frank Ocean");

        final String uri = "/api/albums/" + Integer.toString(id);

        when(albumRepository.findById(id))
                .thenReturn(Optional.of(existingAlbum));

        mockMvc.perform(delete(uri))
                .andExpect(status().isNoContent());

        verify(albumRepository).deleteById(id);
    }

    @Test
    void cannotDeleteAlbum_IfAlbumDoesntExist()  throws Exception {
        final int id = 1;

        when(albumRepository.findById(id))
                .thenReturn(Optional.empty());

        final String uri = "/api/albums/" + Integer.toString(id);

        mockMvc.perform(delete(uri))
                .andExpect(status().isNotFound());

        verify(albumRepository, never()).deleteById(anyInt());
    }




}
