package com.isaaca.musictracker.lastfm.albumsearch;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LastFMAlbumResults {

    @JsonProperty("albummatches")
    public LastFMAlbumMatches albumMatches;

    public LastFMAlbumMatches getAlbumMatches() {
        return albumMatches;
    }

    public void setAlbumMatches(LastFMAlbumMatches albumMatches) {
        this.albumMatches = albumMatches;
    }
}
