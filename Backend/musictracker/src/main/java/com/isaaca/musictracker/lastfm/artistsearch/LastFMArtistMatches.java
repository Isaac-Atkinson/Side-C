package com.isaaca.musictracker.lastfm.artistsearch;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.isaaca.musictracker.entities.Artist;

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
