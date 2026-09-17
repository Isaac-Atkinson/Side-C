import type {AlbumSearchResult} from "../types/AlbumSearchResult.ts";

type AlbumSearchDisplayCardProps = {
    searchResult : AlbumSearchResult;
}

function AlbumSearchDisplayCard({searchResult}: AlbumSearchDisplayCardProps) {
    return (
        <div>
            <h3>{searchResult.title}</h3>
            <p>{searchResult.artist}</p>
            <p>{searchResult.musicBrainzId}</p>
        </div>
    )
}

export default AlbumSearchDisplayCard;