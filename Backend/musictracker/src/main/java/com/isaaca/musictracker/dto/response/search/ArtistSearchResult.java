package com.isaaca.musictracker.dto.response.search;

public record ArtistSearchResult(
        String musicBrainzId,
        String lastFMUrl,
        String name,
        String imageUrl
) {

}
