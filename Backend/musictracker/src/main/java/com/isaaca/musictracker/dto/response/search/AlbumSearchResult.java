package com.isaaca.musictracker.dto.response.search;

public record AlbumSearchResult(
        String musicBrainzId,
        String title,
        String artist,
        String imageUrl
) {}
