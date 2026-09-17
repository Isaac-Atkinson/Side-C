package com.isaaca.musictracker.lastfm;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LastFMAlbumMatches {

    @JsonProperty("album")
    private List<LastFMAlbum> albums;

    public List<LastFMAlbum> getAlbums() {
        return albums;
    }

    public void setAlbums(List<LastFMAlbum> albums) {
        this.albums = albums;
    }
}
