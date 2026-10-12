package com.isaaca.musictracker.dto.response.search;

public record ArtistSearchResult(
        String musicBrainzId,
        String name,
        String imageUrl
) {

}
