package com.isaaca.musictracker.dto.response.search;

import java.util.List;

public record SearchResult(

    List<AlbumSearchResult> albums,
    List<ArtistSearchResult> artists
) {}

