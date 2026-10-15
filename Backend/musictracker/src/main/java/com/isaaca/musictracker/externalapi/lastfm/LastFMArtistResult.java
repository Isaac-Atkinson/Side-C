package com.isaaca.musictracker.externalapi.lastfm;

public record LastFMArtistResult(
        String musicBrainzId,
        String lastFMUrl,
        String name
) {}
