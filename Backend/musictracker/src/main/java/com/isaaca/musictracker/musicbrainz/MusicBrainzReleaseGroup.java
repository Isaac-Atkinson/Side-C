package com.isaaca.musictracker.musicbrainz;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MusicBrainzReleaseGroup {

    private String id;
    private String title;

    @JsonProperty("artist-credit")
    private List<MusicBrainzArtistCredit> artistCredits;

    @JsonProperty("first-release-date")
    private String firstReleaseDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<MusicBrainzArtistCredit> getArtistCredits() {
        return artistCredits;
    }

    public void setArtistCredits(List<MusicBrainzArtistCredit> artistCredits) {
        this.artistCredits = artistCredits;
    }

    public String getFirstReleaseDate() {
        return firstReleaseDate;
    }

    public void setFirstReleaseDate(String firstReleaseDate) {
        this.firstReleaseDate = firstReleaseDate;
    }
}
