
import type {ArtistReview} from "../types/ArtistReview.ts";
import {Button} from "@mui/material";

type ArtistDataProps = {
    review : ArtistReview;
};

function ArtistCard({review}: ArtistDataProps) {
    return (
        <div>
            <h2>{review.artist.name}</h2>
            <p>{review.rating}</p>
            <p>{review.reviewText}</p>

            {/*<Button onClick={handleDelete}>*/}
            {/*    Delete*/}
            {/*</Button>*/}
        </div>
    );
}

export default ArtistCard