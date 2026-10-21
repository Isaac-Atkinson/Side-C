package com.isaaca.musictracker.services;

import com.isaaca.musictracker.externalapi.fanart.FanartArtistThumb;
import com.isaaca.musictracker.externalapi.fanart.FanartResponse;
import com.isaaca.musictracker.externalapi.lastfm.albumsearch.LastFMAlbumSearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class FanartService {

    @Value("${FANART_API_KEY}")
    private String FANART_API_KEY;

    @Value("${fanart.api.root}")
    private String fanartApiRoot;

    private final HttpClient client =
            HttpClient.newHttpClient();

    private final ObjectMapper mapper;

    public FanartService(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public String getFanartImageUrl(String mbid) throws URISyntaxException, IOException, InterruptedException {
        String url =
                 fanartApiRoot +
                        mbid +
                        "?api_key=" +
                        FANART_API_KEY;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        FanartResponse parsedResponse = mapper.readValue(response.body(), FanartResponse.class);

        return parsedResponse.getImages().getFirst().getUrl();
    }
}
