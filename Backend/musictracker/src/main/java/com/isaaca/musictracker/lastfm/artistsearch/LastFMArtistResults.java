package com.isaaca.musictracker.lastfm.artistsearch;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LastFMArtistResults {

    @JsonProperty("artistmatches")
    private LastFMArtistMatches artistMatches;

    public LastFMArtistMatches getArtistMatches() {
        return artistMatches;
    }

    public void setArtistMatches(LastFMArtistMatches artistMatches) {
        this.artistMatches = artistMatches;
    }
}
