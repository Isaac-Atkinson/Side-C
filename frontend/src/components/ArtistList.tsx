import type {ArtistReview} from "../types/ArtistReview.ts";
import ArtistCard from "./cards/ArtistCard.tsx";


type ArtistListProps = {
    artistReviews: ArtistReview[];
};

function ArtistList({ artistReviews }: ArtistListProps) {
    return (

        <div>
            {artistReviews.map(review => (
                <ArtistCard
                    key = {review.id}
                    review={review}
                />
            ))}
        </div>
    );
}



export default ArtistList;