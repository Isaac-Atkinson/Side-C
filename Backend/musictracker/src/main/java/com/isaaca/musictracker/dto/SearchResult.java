package com.isaaca.musictracker.dto;

import java.util.List;

public record SearchResult(

    List<AlbumSearchResult> albums,
    List<ArtistSearchResult> artists
) {}

