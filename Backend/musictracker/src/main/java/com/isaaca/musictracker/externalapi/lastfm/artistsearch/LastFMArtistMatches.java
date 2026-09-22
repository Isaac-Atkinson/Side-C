package com.isaaca.musictracker.externalapi.lastfm.artistsearch;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LastFMArtistMatches {

    @JsonProperty("artist")
    private List<LastFMArtist> artists;

    public List<LastFMArtist> getArtists() {
        return artists;
    }

    public void setArtists(List<LastFMArtist> artists) {
        this.artists = artists;
    }
}
