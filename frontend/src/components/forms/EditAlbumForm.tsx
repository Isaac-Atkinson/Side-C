import {Box, Button, TextField} from "@mui/material";
import {useState} from "react";
import {API_BASE_URL, MAX_REVIEW_LENGTH} from "../../config/api.ts";
import type {AlbumReview} from "../../types/AlbumReview.ts";

type EditAlbumFormProps = {
    albumReview: AlbumReview;
    onCancel: () => void;
    onAlbumUpdated: (albumReview : AlbumReview) => void;
    onSuccess: () => void;
}
function EditAlbumForm({albumReview, onCancel, onAlbumUpdated, onSuccess}: EditAlbumFormProps) {
    const[rating, setRating] = useState("");
    const[reviewText, setReviewText] = useState("");

    const [ratingError, setRatingError] = useState("");
    const [reviewTextError, setReviewTextError] = useState("");

    const validateForm = () => {
        let valid = true;

        setRatingError("");
        setReviewTextError("");

        const ratingNumber = Number(rating);

        if (!rating) {
            setRatingError("Rating is required");
            valid = false;
        } else if (ratingNumber < 1 || ratingNumber > 10) {
            setRatingError("Rating must be between 0 and 10");
            valid = false;
        }

        return valid;
    }

    const handleSubmit = async (e: React.SubmitEvent) => {
        e.preventDefault();

        if(!validateForm()) return;

        const response = await fetch(`${API_BASE_URL}/albums/reviews/${albumReview.id}`, {
            method: "PUT",
            headers: {"Content-Type": "application/json"},
            credentials: "include",
            body: JSON.stringify({
                rating: Number(rating),
                reviewText: reviewText
            })
        });
        if (response.ok) {
            const updatedAlbumReview: AlbumReview = await response.json();
            onAlbumUpdated(updatedAlbumReview);

            onSuccess();

            console.log("Successfully updated album");
        } else if(response.status === 404){
            console.log("Album not found");
        } else {
            console.log("Something went wrong");
        }
    }

    return (
        <div>
            <Box
                component="form"
                onSubmit={handleSubmit}
                sx={{ display: "flex", flexDirection: "column", gap: 2 }}
            >
                <TextField
                    label = "Rating"
                    type = "number"
                    value = {rating}
                    onChange={e => setRating(e.target.value)}
                    error = {Boolean(ratingError)}
                    helperText={ratingError}
                />

                <TextField
                    label = "Review"
                    value = {reviewText}
                    onChange={e => setReviewText(e.target.value)}
                    error = {Boolean(reviewTextError)}
                    slotProps={{
                        htmlInput: {
                            maxLength: MAX_REVIEW_LENGTH
                        }
                    }}
                    helperText={`${reviewText.length}/${MAX_REVIEW_LENGTH}`}
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

export default EditAlbumForm;