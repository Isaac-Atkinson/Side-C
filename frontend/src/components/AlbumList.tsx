import AlbumCard from "./AlbumCard.tsx";
import type {AlbumReview} from "../types/AlbumReview.ts";


type AlbumListProps = {
    albumReviews: AlbumReview[];
    onAlbumDeleted: (id: number) => void;
};

function AlbumList({albumReviews, onAlbumDeleted}: AlbumListProps) {
    return (

        <div>
            {albumReviews.map(review => (
                <AlbumCard
                    key={review.id}
                    albumReview={review}
                    onAlbumDeleted = {onAlbumDeleted}
                />
            ))}
        </div>
    );
}



export default AlbumList;