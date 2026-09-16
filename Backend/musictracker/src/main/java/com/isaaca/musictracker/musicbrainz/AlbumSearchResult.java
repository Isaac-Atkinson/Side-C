package com.isaaca.musictracker.musicbrainz;

public record AlbumSearchResult(
        String externalId,
        String title,
        String artist,
        Integer year
) {}
