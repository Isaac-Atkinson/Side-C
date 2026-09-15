import {useState} from "react";
import {API_BASE_URL, MAX_REVIEW_LENGTH} from "../../config/api.ts";
import {Alert, Box, Button, TextField} from "@mui/material";
import type {ArtistReview} from "../../types/ArtistReview.ts";

type AddArtistFormProps = {
    onArtistAdded : (newArtistReview : ArtistReview) => void;
    onSuccess: () => void;
    onCancel: () => void;
}

function AddArtistForm({onArtistAdded, onSuccess, onCancel}: AddArtistFormProps) {
    const[name, setName] = useState("");
    const[rating, setRating] = useState("");
    const[reviewText, setReviewText] = useState("");

    const [nameError, setNameError] = useState("");
    const [ratingError, setRatingError] = useState("");
    const [reviewTextError, setReviewTextError] = useState("");
    const [submitError, setSubmitError] = useState("");

    const validateForm = () => {
        let valid = true;

        setNameError("");
        setRatingError("");
        setReviewTextError("");

        if (!name.trim()) {
            setNameError("Title is required");
            valid = false;
        }

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
        setSubmitError("");

        if(!validateForm()) return;

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

            onSuccess();

            console.log("Success");
        } else if(response.status === 409){
            setSubmitError("You have already reviewed this artist");
        } else if(response.status === 400){
            setSubmitError("Invalid information entered");
        } else {
            setSubmitError("Something went wrong");
        }

    }

    return (
        <Box
            component="form"
            onSubmit={handleSubmit}
            sx={{ display: "flex", flexDirection: "column", gap: 2 }}
        >
            <TextField
                label = "Name"
                value = {name}
                onChange={e => setName(e.target.value)}
                error = {Boolean(nameError)}
                helperText={nameError}
            />

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
                error={Boolean(reviewTextError)}
                slotProps={{
                    htmlInput: {
                        maxLength: MAX_REVIEW_LENGTH
                    }
                }}
                helperText={`${reviewText.length}/${MAX_REVIEW_LENGTH}`}

            />

            <Button type="submit">
                Add Artist
            </Button>

            <Button onClick={onCancel}>
                Cancel
            </Button>

            {submitError && (
                <Alert severity="error">
                    {submitError}
                </Alert>
            )}
        </Box>
    )


}

export default AddArtistForm;