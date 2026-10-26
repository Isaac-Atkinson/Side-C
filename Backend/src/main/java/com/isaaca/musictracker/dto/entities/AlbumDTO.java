package com.isaaca.musictracker.dto.entities;

import com.isaaca.musictracker.entities.Album;

public record AlbumDTO(
    Integer id,
    String title,
    ArtistDTO artist,
    String imageUrl
)
{
    public static AlbumDTO from(Album album) {
        return new AlbumDTO(
                album.getId(),
                album.getTitle(),
                ArtistDTO.from(album.getArtist()),
                album.getImageUrl()
        );
    }
}
