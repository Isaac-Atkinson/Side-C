package com.isaaca.musictracker.dto.response.search;

public record AlbumSearchResult(
        String musicBrainzId,
        String lastFMUrl,
        String title,
        String artist,
        String imageUrl
) {}
