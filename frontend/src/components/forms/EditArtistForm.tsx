import type {ArtistReview} from "../../types/ArtistReview.ts";
import {useState} from "react";
import {API_BASE_URL, MAX_REVIEW_LENGTH} from "../../config/api.ts";
import {Box, Button, TextField} from "@mui/material";

type EditAlbumFormProps = {
    artistReview: ArtistReview;
    onCancel: () => void;
    onArtistUpdated: (artistReview: ArtistReview) => void;
    onSuccess : () => void;
}

function EditArtistForm({ artistReview, onCancel, onArtistUpdated, onSuccess }: EditAlbumFormProps) {
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
            onSuccess();

            console.log("Successfully updated artist");
        }
    }
    return(
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
                    label = "Text"
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

export default EditArtistForm;