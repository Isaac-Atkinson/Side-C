import type {ArtistReview} from "../types/ArtistReview.ts";
import ArtistReviewCard from "./cards/ArtistReviewCard.tsx";
import {Box} from "@mui/material";


type ArtistListProps = {
    artistReviews: ArtistReview[];
};

function ArtistList({ artistReviews }: ArtistListProps) {
    return (

        <div>
            <Box
                sx={{
                    display: "grid",
                    gridTemplateColumns: "repeat(auto-fill, minmax(220px, 1fr))",
                    gap: 2,
                    mt: 2
                }}
            >
                {artistReviews.map(review => (
                    <ArtistReviewCard
                        key = {review.id}
                        review={review}
                    />
                ))}
            </Box>

        </div>
    );
}



export default ArtistList;