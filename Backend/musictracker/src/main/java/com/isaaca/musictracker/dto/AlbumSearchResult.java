package com.isaaca.musictracker.dto;

public record AlbumSearchResult(
        String musicBrainzId,
        String title,
        String artist,
        String imageUrl
) {}
