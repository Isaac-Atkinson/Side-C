package com.isaaca.musictracker.lastfm.albumsearch;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LastFMAlbumSearchResponse {

    @JsonProperty("results")
    public LastFMResults lastFMResults;

    public LastFMResults getLastFMResults() {
        return lastFMResults;
    }

    public void setLastFMResults(LastFMResults lastFMResults) {
        this.lastFMResults = lastFMResults;
    }
}
