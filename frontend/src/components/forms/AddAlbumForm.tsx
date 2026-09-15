import {useState} from "react";
import { Box, Button, TextField } from "@mui/material";
import {API_BASE_URL, MAX_REVIEW_LENGTH} from "../../config/api.ts";
import type {AlbumReview} from "../../types/AlbumReview.ts";


type AddAlbumFormProps = {
    onAlbumAdded : (newAlbumReview : AlbumReview) => void;
    onSuccess: () => void;
    onCancel: () => void;
}

function AddAlbumForm({onAlbumAdded, onSuccess, onCancel}: AddAlbumFormProps) {
    const[title, setTitle] = useState("");
    const[artist, setArtist] = useState("");
    const[year, setYear] = useState("");
    const[rating, setRating] = useState("");
    const[reviewText, setReviewText] = useState("");

    const [titleError, setTitleError] = useState("");
    const [artistError, setArtistError] = useState("");
    const [yearError, setYearError] = useState("");
    const [ratingError, setRatingError] = useState("");
    const [reviewTextError, setReviewTextError] = useState("");

    const validateForm= () => {
        let valid = true;

        setTitleError("");
        setArtistError("");
        setYearError("");
        setRatingError("");
        setReviewTextError("");

        if (!title.trim()) {
            setTitleError("Title is required");
            valid = false;
        }

        if (!artist.trim()) {
            setArtistError("Artist is required");
            valid = false;
        }

        if (!year) {
            setYearError("Year is required");
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

        if(!validateForm()) return;

        const response = await fetch(`${API_BASE_URL}/albums/reviews`, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({
                title: title,
                artist: artist,
                year: Number (year),
                rating: Number (rating),
                reviewText: reviewText,
            })
        });

        if(response.ok){
            const newAlbumReview: AlbumReview = await response.json();
            onAlbumAdded(newAlbumReview);

            onSuccess();
            console.log("Successfully added album");
        }

    }




    return (
        <Box
            component="form"
            onSubmit={handleSubmit}
            sx={{ display: "flex", flexDirection: "column", gap: 2 }}
        >
            <TextField
                label = "Title"
                value = {title}
                onChange={e => setTitle(e.target.value)}
                error={Boolean(titleError)}
                helperText={titleError}
            />

            <TextField
                label = "Artist"
                value = {artist}
                onChange={e => setArtist(e.target.value)}
                error={Boolean(artistError)}
                helperText={artistError}
            />

            <TextField
                label = "Year"
                type = "number"
                value = {year}
                onChange={e => setYear(e.target.value)}
                error={Boolean(yearError)}
                helperText={yearError}
            />

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
        </Box>
    );
}

export default AddAlbumForm;