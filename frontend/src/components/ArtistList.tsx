import type {ArtistReview} from "../types/ArtistReview.ts";
import ArtistCard from "./ArtistCard.tsx";


type ArtistListProps = {
    artistReviews: ArtistReview[];
    onArtistDeleted: (id : number) => void;
};

function ArtistList({ artistReviews, onArtistDeleted }: ArtistListProps) {
    return (

        <div>
            {artistReviews.map(review => (
                <ArtistCard
                    key = {review.id}
                    review={review}
                    onArtistDeleted = {onArtistDeleted}
                />
            ))}
        </div>
    );
}



export default ArtistList;