package com.isaaca.musictracker;

public record AlbumSearchResult(
        String musicBrainzId,
        String title,
        String artist,
        String imageUrl
) {}
