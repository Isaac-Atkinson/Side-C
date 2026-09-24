package com.isaaca.musictracker.services;

import com.isaaca.musictracker.AlbumSearchResult;
import com.isaaca.musictracker.ArtistSearchResult;
import com.isaaca.musictracker.LastFMArtistResult;
import com.isaaca.musictracker.externalapi.lastfm.albuminfo.LastFMAlbumInfo;
import com.isaaca.musictracker.externalapi.lastfm.albuminfo.LastFMResponseAlbumInfo;
import com.isaaca.musictracker.externalapi.lastfm.albumsearch.LastFMAlbumSearchResponse;
import com.isaaca.musictracker.externalapi.lastfm.artistinfo.LastFMArtistInfo;
import com.isaaca.musictracker.externalapi.lastfm.artistinfo.LastFMResponseArtistInfo;
import com.isaaca.musictracker.externalapi.lastfm.artistsearch.LastFMArtist;
import com.isaaca.musictracker.externalapi.lastfm.artistsearch.LastFMArtistSearchResponse;
import com.isaaca.musictracker.requests.AlbumRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class LastFMService {

    @Value("${API_KEY}")
    private String API_KEY;

    @Value("${lastfm.api.root}")
    private String lastFMApiRoot;

    private final int ALBUM_REQUEST_LIMIT = 16;

    private final int ARTIST_REQUEST_LIMIT = 4;

    private final HttpClient client =
            HttpClient.newHttpClient();

    private final ObjectMapper mapper;

    public LastFMService(ObjectMapper mapper) {
        this.mapper = mapper;
    }


    public List<AlbumSearchResult> lookUpAlbums(String title) throws IOException, InterruptedException, URISyntaxException {


        String url =
                lastFMApiRoot +
                        "?method=album.search" +
                        "&album=" + URLEncoder.encode(title, StandardCharsets.UTF_8) +
                        "&api_key=" + API_KEY +
                        "&limit=" + Integer.toString(ALBUM_REQUEST_LIMIT) +
                        "&format=json";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header(
                        "User-Agent",
                        "MusicTracker/1.0 (iatkinson18@outlook.com)"
                )
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        LastFMAlbumSearchResponse parsedResponse = mapper.readValue(response.body(), LastFMAlbumSearchResponse.class);

        List<AlbumSearchResult> results = parsedResponse
                .getLastFMResults()
                .getAlbumMatches()
                .getAlbums()
                .stream()
                .map(album -> new AlbumSearchResult(
                        album.getMbid(),
                        album.getName(),
                        album.getArtist(),
                        album.getImages().getLast().getImageUrl()
                )).toList();

        return results;
    }

    public LastFMAlbumInfo getAlbumInfo(AlbumRequest albumRequest) throws IOException, URISyntaxException, InterruptedException {


        if(albumRequest.getMusicBrainzId() != null && !albumRequest.getMusicBrainzId().isBlank()){

            return getAlbumInfoByMbid(albumRequest.getMusicBrainzId());
        } else {

            return getAlbumInfoByAlbumAndArtist(albumRequest.getTitle(), albumRequest.getArtist());
        }
    }

    public List<LastFMArtistResult> lookUpArtists(String name) throws IOException, URISyntaxException, InterruptedException {
        String url =
                lastFMApiRoot +
                        "?method=artist.search" +
                        "&artist=" + URLEncoder.encode(name, StandardCharsets.UTF_8) +
                        "&api_key=" + API_KEY +
                        "&limit=" + Integer.toString(ARTIST_REQUEST_LIMIT) +
                        "&format=json";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header(
                        "User-Agent",
                        "MusicTracker/1.0 (iatkinson18@outlook.com)"
                )
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        LastFMArtistSearchResponse parsedResponse = mapper.readValue(response.body(), LastFMArtistSearchResponse.class);

        List<LastFMArtistResult> results = parsedResponse
                .getResults()
                .getArtistMatches()
                .getArtists()
                .stream()
                .map(artist -> new LastFMArtistResult(
                        artist.getMbid(),
                        artist.getName()
                )).toList();

        return results;
    }

    public LastFMArtistInfo getArtistInfo(String artistName) throws URISyntaxException, IOException, InterruptedException {
        String url =
                lastFMApiRoot +
                        "?method=artist.getinfo" +
                        "&artist=" + URLEncoder.encode(artistName, StandardCharsets.UTF_8) +
                        "&api_key=" + API_KEY +
                        "&format=json";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header(
                        "User-Agent",
                        "MusicTracker/1.0 (iatkinson18@outlook.com)"
                )
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        LastFMResponseArtistInfo parsedResponse = mapper.readValue(response.body(), LastFMResponseArtistInfo.class);

        return parsedResponse.getArtist();
    }

    private LastFMAlbumInfo getAlbumInfoByMbid(String mbid) throws IOException, URISyntaxException, InterruptedException {


        String url =
                lastFMApiRoot +
                        "?method=album.getinfo" +
                        "&mbid=" + URLEncoder.encode(mbid, StandardCharsets.UTF_8) +
                        "&api_key=" + API_KEY +
                        "&format=json";

        return sendAlbumInfoRequest(url);
    }

    private LastFMAlbumInfo getAlbumInfoByAlbumAndArtist(String album, String artist) throws IOException, URISyntaxException, InterruptedException {


        String url =
                lastFMApiRoot +
                        "?method=album.getinfo" +
                        "&album=" + URLEncoder.encode(album, StandardCharsets.UTF_8) +
                        "&artist=" + URLEncoder.encode(artist, StandardCharsets.UTF_8) +
                        "&api_key=" + API_KEY +
                        "&format=json";

        return sendAlbumInfoRequest(url);
    }

    private LastFMAlbumInfo sendAlbumInfoRequest(String url) throws URISyntaxException, IOException, InterruptedException {


        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header(
                        "User-Agent",
                        "MusicTracker/1.0 (iatkinson18@outlook.com)"
                )
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        LastFMResponseAlbumInfo parsedResponse = mapper.readValue(response.body(), LastFMResponseAlbumInfo.class);

        return parsedResponse.getAlbum();
    }

}
