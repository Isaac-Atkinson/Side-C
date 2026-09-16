package com.isaaca.musictracker.musicbrainz;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MusicBrainzResponse {

    @JsonProperty("release-groups")
    public List<MusicBrainzReleaseGroup> releaseGroups;

    public List<MusicBrainzReleaseGroup> getReleaseGroups() {
        return releaseGroups;
    }

    public void setReleaseGroups(List<MusicBrainzReleaseGroup> releaseGroups) {
        this.releaseGroups = releaseGroups;
    }
}
