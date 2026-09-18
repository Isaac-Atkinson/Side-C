package com.isaaca.musictracker.lastfm.albumsearch;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LastFMResults {

    @JsonProperty("albummatches")
    public LastFMAlbumMatches albumMatches;

    public LastFMAlbumMatches getAlbumMatches() {
        return albumMatches;
    }

    public void setAlbumMatches(LastFMAlbumMatches albumMatches) {
        this.albumMatches = albumMatches;
    }
}
