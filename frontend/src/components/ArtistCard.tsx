
import type {ArtistReview} from "../types/ArtistReview.ts";
import {Link} from "react-router-dom";

type ArtistDataProps = {
    review : ArtistReview;
};

function ArtistCard({review}: ArtistDataProps) {

    return (
        <div>
            <Link to={`/artists/${review.id}`}>
                <h2>{review.artist.name}</h2>
            </Link>

            <p>{review.rating}</p>


        </div>
    );
}

export default ArtistCard