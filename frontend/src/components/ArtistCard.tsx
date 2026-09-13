
import type {ArtistReview} from "../types/ArtistReview.ts";
import {Button} from "@mui/material";
import {API_BASE_URL} from "../config/api.ts";

type ArtistDataProps = {
    review : ArtistReview;
    onArtistDeleted: (id : number) => void;
};

function ArtistCard({review, onArtistDeleted}: ArtistDataProps) {

    const handleDelete = async () => {
        const response = await fetch(`${API_BASE_URL}/artists/reviews/${review.id}`,
            {
                method : "DELETE"
            }
        );

        if(response.ok){
            onArtistDeleted(review.id);
            console.log("Successfully deleted album");
        } else {
            console.log("Error while deleting album");
        }
    }


    return (
        <div>
            <h2>{review.artist.name}</h2>
            <p>{review.rating}</p>
            <p>{review.reviewText}</p>

            <Button onClick={handleDelete}>
                Delete
            </Button>
        </div>
    );
}

export default ArtistCard