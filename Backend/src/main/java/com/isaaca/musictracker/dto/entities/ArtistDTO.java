package com.isaaca.musictracker.dto.entities;

import com.isaaca.musictracker.entities.Artist;

public record ArtistDTO(
    Integer id,
    String name,
    String imageUrl
) {
    public static  ArtistDTO from(Artist artist) {
        return new ArtistDTO(
                artist.getId(),
                artist.getName(),
                artist.getImageUrl()
        );
    }
}
