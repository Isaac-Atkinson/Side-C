import type {ArtistReview} from "../../types/ArtistReview.ts";
import {useState} from "react";
import {API_BASE_URL} from "../../config/api.ts";
import {Box, Button, TextField} from "@mui/material";

type EditAlbumFormProps = {
    artistReview: ArtistReview;
    onCancel: () => void;
    onArtistUpdated: (artistReview: ArtistReview) => void;
}

function EditArtistForm({ artistReview, onCancel, onArtistUpdated }: EditAlbumFormProps) {
    const[rating, setRating] = useState("");
    const[reviewText, setReviewText] = useState("");

    const handleSubmit = async (e: React.SubmitEvent) => {
        e.preventDefault();
        const response = await fetch(`${API_BASE_URL}/artists/reviews/${artistReview.id}`, {
            method: "PUT",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({
                name : artistReview.artist.name,
                rating: Number(rating),
                reviewText : reviewText
            })
        });

        if (response.ok) {
            const updatedArtistReview: ArtistReview = await response.json();
            onArtistUpdated(updatedArtistReview);

            console.log("Successfully updated artist");
        }
    }
    return(
        <div>
            <Box
                component="form"
                onSubmit={handleSubmit}
            >
                <TextField
                    label = "Rating"
                    type = "number"
                    value = {rating}
                    onChange={e => setRating(e.target.value)}
                />

                <TextField
                    label = "Text"
                    value = {reviewText}
                    onChange={e => setReviewText(e.target.value)}
                />

                <Button type="submit">
                    Submit
                </Button>

                <Button onClick={onCancel}>
                    Cancel
                </Button>
            </Box>
        </div>
    )
}

export default EditArtistForm;