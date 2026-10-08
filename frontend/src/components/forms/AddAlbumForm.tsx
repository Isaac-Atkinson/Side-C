import {useState} from "react";
import {Alert, Box, Button, TextField} from "@mui/material";
import {API_BASE_URL, MAX_REVIEW_LENGTH} from "../../config/api.ts";
import type {AlbumSearchResult} from "../../types/AlbumSearchResult.ts";


type AddAlbumFormProps = {
    album : AlbumSearchResult
    onSuccess: () => void;
    onCancel: () => void;
}

function AddAlbumForm({album, onSuccess, onCancel}: AddAlbumFormProps) {
    const[rating, setRating] = useState("");
    const[reviewText, setReviewText] = useState("");

    const [ratingError, setRatingError] = useState("");
    const [reviewTextError, setReviewTextError] = useState("");
    const [submitError, setSubmitError] = useState("");

    const validateForm= () => {
        let valid = true;


        setRatingError("");
        setReviewTextError("");

        const ratingNumber = Number(rating);

        if (!rating) {
            setRatingError("Rating is required");
            valid = false;
        } else if (ratingNumber < 1 || ratingNumber > 10) {
            setRatingError("Rating must be between 1 and 10");
            valid = false;
        }

        return valid;
    }

    const handleSubmit = async (e: React.SubmitEvent) => {
        e.preventDefault();

        setSubmitError("");
        if(!validateForm()) return;

        const response = await fetch(`${API_BASE_URL}/albums/reviews`, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            credentials: "include",
            body: JSON.stringify({
                musicBrainzId: album.musicBrainzId,
                title: album.title,
                artist: album.artist,
                rating: Number (rating),
                reviewText: reviewText
            })
        });

        if(response.ok){
            //const newAlbumReview: AlbumReview = await response.json();
            // onAlbumAdded(newAlbumReview);

            onSuccess();
            console.log("Successfully added album");
        } else if(response.status === 409){
            setSubmitError("You have already reviewed this album");
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
            <h3>{album.title}</h3>
            <h4>{album.artist}</h4>
            <TextField
                label = "Rating"
                type = "number"
                value = {rating}
                onChange={e => setRating(e.target.value)}
                error={Boolean(ratingError)}
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
                Add Album
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
    );
}

export default AddAlbumForm;