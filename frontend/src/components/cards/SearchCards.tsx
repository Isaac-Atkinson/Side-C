import type {AlbumSearchResult} from "../../types/AlbumSearchResult.ts";
import MediaCard from "./MediaCard.tsx";
import type {ArtistSearchResult} from "../../types/ArtistSearchResult.ts";
import {AlbumPlaceholder, ArtistPlaceholder} from "../placeholders/placeholders.tsx";

type AlbumSearchDisplayCardProps = {
    searchResult : AlbumSearchResult;
    onClick : () => void;
}

export function AlbumSearchDisplayCard({searchResult, onClick}: AlbumSearchDisplayCardProps) {
    return (
        <MediaCard
            title = {searchResult.title}
            imageUrl = {searchResult.imageUrl}
            imageAlt={`${searchResult.title}`}
            onClick = {onClick}
            placeholder={AlbumPlaceholder}

            link={searchResult.reviewed ? `/albums/${searchResult.reviewId}` : null}
        />
    )
}



type ArtistSearchDisplayCardProps = {
    searchResult : ArtistSearchResult;
    onClick : () => void;
}

export function ArtistSearchDisplayCard({searchResult, onClick}: ArtistSearchDisplayCardProps) {
    return (
        <MediaCard
            title = {searchResult.name}
            imageUrl = {searchResult.imageUrl}
            imageAlt={`${searchResult.name}`}
            onClick = {onClick}
            placeholder={ArtistPlaceholder}

            link={searchResult.reviewed ? `/artists/${searchResult.reviewId}` : null}
        />
    )
}

