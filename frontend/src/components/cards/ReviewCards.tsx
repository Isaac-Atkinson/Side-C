import type {AlbumReview} from "../../types/AlbumReview.ts";
import MediaCard from "./MediaCard.tsx";
import AlbumIcon from "@mui/icons-material/Album";
import type {ArtistReview} from "../../types/ArtistReview.ts";
import PersonIcon from "@mui/icons-material/Person";

type AlbumReviewCardProps = {
    albumReview: AlbumReview;
}
export function AlbumReviewCard({albumReview} : AlbumReviewCardProps) {
    return (
        <MediaCard
            title={albumReview.album.title}
            subtitle={albumReview.album.artist.name}
            rating={albumReview.rating}
            imageUrl={albumReview.album.imageUrl}
            imageAlt={`${albumReview.album.title} cover`}
            link = {`/albums/${albumReview.id}`}
            placeholder={<AlbumIcon fontSize="large" />}
        />
    )
}

type ArtistReviewCardProps = {
    artistReview : ArtistReview;
};

export function ArtistReviewCard({artistReview}: ArtistReviewCardProps) {


    return (
        <MediaCard
            title={artistReview.artist.name}
            rating={artistReview.rating}
            imageUrl={artistReview.artist.imageUrl}
            imageAlt={`${artistReview.artist.name}`}
            link = {`/artists/${artistReview.id}`}
            placeholder={<PersonIcon fontSize="large" />}
        />
    )
}