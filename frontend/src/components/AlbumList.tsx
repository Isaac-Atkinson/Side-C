import AlbumCard from "./AlbumCard.tsx";
import type {AlbumReview} from "../types/AlbumReview.ts";


type AlbumListProps = {
    albumReviews: AlbumReview[];
};

function AlbumList({albumReviews}: AlbumListProps) {
    return (

        <div>
            {albumReviews.map(review => (
                <AlbumCard
                    key={review.id}
                    albumReview={review}
                />
            ))}
        </div>
    );
}



export default AlbumList;