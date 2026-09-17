package com.isaaca.musictracker.lastfm;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LastFMResponse {

    @JsonProperty("results")
    public LastFMResults lastFMResults;

    public LastFMResults getLastFMResults() {
        return lastFMResults;
    }

    public void setLastFMResults(LastFMResults lastFMResults) {
        this.lastFMResults = lastFMResults;
    }
}
