package com.isaaca.musictracker.lastfm.albuminfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.isaaca.musictracker.lastfm.albumsearch.LastFMImage;

import java.util.List;

public class LastFMAlbumInfo {

    @JsonProperty("image")
    private List<LastFMImage> images;

    public List<LastFMImage> getImages() {
        return images;
    }

    public void setImages(List<LastFMImage> images) {
        this.images = images;
    }
}
