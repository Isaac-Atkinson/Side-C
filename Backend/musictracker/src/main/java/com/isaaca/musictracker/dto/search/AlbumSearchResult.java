package com.isaaca.musictracker.dto.search;

public record AlbumSearchResult(
        String musicBrainzId,
        String title,
        String artist,
        String imageUrl
) {}
