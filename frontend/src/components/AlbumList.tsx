import AlbumCard from "./cards/AlbumCard.tsx";
import type {AlbumReview} from "../types/AlbumReview.ts";
import {Box} from "@mui/material";


type AlbumListProps = {
    albumReviews: AlbumReview[];
};

function AlbumList({albumReviews}: AlbumListProps) {
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
                {albumReviews.map(review => (
                    <AlbumCard
                        key={review.id}
                        albumReview={review}
                    />
                ))}
            </Box>

        </div>
    );
}



export default AlbumList;