package com.isaaca.musictracker.externalapi.lastfm.artistsearch;

public class LastFMArtist {

    private String name;

    private String mbid;

    private String lastFMUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public String getLastFMUrl() {
        return lastFMUrl;
    }

    public void setLastFMUrl(String lastFMUrl) {
        this.lastFMUrl = lastFMUrl;
    }
}
