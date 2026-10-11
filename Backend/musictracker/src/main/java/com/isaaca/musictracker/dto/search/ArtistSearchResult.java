package com.isaaca.musictracker.dto.search;

public record ArtistSearchResult(
        String musicBrainzId,
        String name,
        String imageUrl
) {

}
