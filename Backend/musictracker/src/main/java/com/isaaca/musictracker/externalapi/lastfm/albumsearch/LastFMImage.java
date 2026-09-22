package com.isaaca.musictracker.externalapi.lastfm.albumsearch;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LastFMImage {

    @JsonProperty("#text")
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
