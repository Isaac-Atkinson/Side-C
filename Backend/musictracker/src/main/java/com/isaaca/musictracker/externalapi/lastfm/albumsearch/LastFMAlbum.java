package com.isaaca.musictracker.externalapi.lastfm.albumsearch;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LastFMAlbum {

    private String name;

    private String artist;

    private String mbid;

    @JsonProperty("image")
    private List<LastFMImage> images;

    public List<LastFMImage> getImages() {
        return images;
    }

    public void setImages(List<LastFMImage> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }
}
