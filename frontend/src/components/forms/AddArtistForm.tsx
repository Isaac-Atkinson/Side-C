import {useState} from "react";
import {API_BASE_URL} from "../../config/api.ts";
import {Box, Button, TextField} from "@mui/material";
import type {ArtistReview} from "../../types/ArtistReview.ts";

type AddArtistFormProps = {
    onArtistAdded : (newArtistReview : ArtistReview) => void;
}

function AddArtistForm({onArtistAdded}: AddArtistFormProps) {
    const[name, setName] = useState("");
    const[rating, setRating] = useState("");
    const[reviewText, setReviewText] = useState("");

    const handleSubmit = async (e: React.SubmitEvent) => {
        e.preventDefault();
        const response = await fetch(`${API_BASE_URL}/artists/reviews`, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({
                name: name,
                rating: Number (rating),
                reviewText: reviewText,
            })
        });

        if(response.ok){
            const newArtistReview: ArtistReview = await response.json();

            onArtistAdded(newArtistReview);
            console.log("Success");
        }

    }

    return (
        <Box
            component="form"
            onSubmit={handleSubmit}
        >
            <TextField
                label = "Name"
                value = {name}
                onChange={e => setName(e.target.value)}
            />

            <TextField
                label = "Rating"
                type = "number"
                value = {rating}
                onChange={e => setRating(e.target.value)}
            />

            <TextField
                label = "Review Text"
                value = {reviewText}
                onChange={e => setReviewText(e.target.value)}
            />

            <Button type="submit">
                Add Artist
            </Button>
        </Box>
    )


}

export default AddArtistForm;