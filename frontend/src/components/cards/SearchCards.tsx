import type {AlbumSearchResult} from "../../types/AlbumSearchResult.ts";
import MediaCard from "./MediaCard.tsx";
import AlbumIcon from "@mui/icons-material/Album";

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
            placeholder={<AlbumIcon fontSize="large" />}
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
            placeholder={<AlbumIcon fontSize="large" />}
        />
    )
}

