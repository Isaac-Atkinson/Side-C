package com.isaaca.musictracker;

import java.util.ArrayList;
import java.util.List;

public record SearchResult(

    List<AlbumSearchResult> albums,
    List<ArtistSearchResult> artists
) {}

