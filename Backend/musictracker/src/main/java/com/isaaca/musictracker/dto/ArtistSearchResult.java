package com.isaaca.musictracker.dto;

public record ArtistSearchResult(
        String musicBrainzId,
        String name,
        String imageUrl
) {

}
