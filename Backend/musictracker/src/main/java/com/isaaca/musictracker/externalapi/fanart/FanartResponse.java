package com.isaaca.musictracker.externalapi.fanart;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FanartResponse {

    @JsonProperty("artistthumb")
    public List<FanartArtistThumb> images;

    public List<FanartArtistThumb> getImages() {
        return images;
    }

    public void setImages(List<FanartArtistThumb> image) {
        this.images = image;
    }
}
