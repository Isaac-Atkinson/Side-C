import {useNavigate} from "react-router-dom";
import { useState} from "react";
import {API_BASE_URL} from "../config/api.ts";
import type {ArtistReview} from "../types/ArtistReview.ts";
import {Button} from "@mui/material";
import EditArtistForm from "./forms/EditArtistForm.tsx";


type DetailedArtistCardProps = {
    artistReview: ArtistReview;
    onArtistUpdated: (artistReview: ArtistReview) => void;
}
function DetailedArtistCard({artistReview, onArtistUpdated}: DetailedArtistCardProps) {
    const navigate = useNavigate();
    const [editing, setEditing] = useState(false);

    const handleDelete = async () => {


        const response = await fetch(`${API_BASE_URL}/artists/reviews/${artistReview.id}`,
            {
                method : "DELETE"
            }
        );

        if(response.ok){
            console.log("Successfully deleted artist");
            navigate("/artists");
        } else {
            console.log("Error while deleting artist");
        }
    }
    return (
        <div>
            <h2>{artistReview.artist.name}</h2>
            <p>{artistReview.rating}</p>
            <p>{artistReview.reviewText}</p>
            <Button onClick={handleDelete}>
                Delete
            </Button>

            <Button onClick ={() => {
                setEditing(true);
            }}>
                Edit
            </Button>

            {editing && (
                <EditArtistForm artistReview={artistReview}
                               onCancel={() => setEditing(false)}
                               onArtistUpdated={onArtistUpdated}
                />
            )}
        </div>
    )
}

export default DetailedArtistCard;