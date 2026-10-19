package com.isaaca.musictracker.externalapi.lastfm;

public record LastFMAlbumSearchResult(
        String musicBrainzId,
        String lastFMUrl,
        String title,
        String artist,
        String imageUrl
) {
}
