package com.isaaca.musictracker.lastfm.albumsearch;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LastFMAlbumSearchResponse {

    @JsonProperty("results")
    public LastFMAlbumResults lastFMResults;

    public LastFMAlbumResults getLastFMResults() {
        return lastFMResults;
    }

    public void setLastFMResults(LastFMAlbumResults lastFMResults) {
        this.lastFMResults = lastFMResults;
    }
}
