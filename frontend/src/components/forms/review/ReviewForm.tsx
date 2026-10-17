import {useState} from "react";
import {Alert, Box, Button, Rating, TextField, Typography} from "@mui/material";
import {MAX_REVIEW_LENGTH} from "../../../config/api.ts";

type ReviewFormProps = {
    title: string;
    subtitle?: string;
    existingRating?: number
    existingReviewText?: string;
    imageUrl?: string;
    onSubmit: (rating : number, reviewText: string) => Promise<string | null>;
    onCancel: () => void;
}

function ReviewForm({title, subtitle, existingRating, existingReviewText, imageUrl, onSubmit, onCancel}: ReviewFormProps) {
    const[rating, setRating] = useState<number | null>(existingRating ? existingRating : null);
    const[reviewText, setReviewText] = useState(existingReviewText ? existingReviewText : "");

    const [ratingError, setRatingError] = useState("");
    const [reviewTextError, setReviewTextError] = useState("");
    const [submitError, setSubmitError] = useState("");

    const validateForm = () => {
        let valid = true;

        setRatingError("");
        setReviewTextError("");

        if (!rating) {
            setRatingError("Rating is required");
            valid = false;
        }
        return valid;
    }

    const handleSubmit = async (e : React.SubmitEvent) => {
        e.preventDefault();

        if(!validateForm()) return;

        const error = await onSubmit(Number (rating), reviewText);

        if (error) {
            setSubmitError(error);
        }
    }


    return (
        <Box
            component="form"
            onSubmit={handleSubmit}
            sx={{
                display: "flex",
                flexDirection: "column",
                gap: 2
        }}
        >
            <Box
                component="img"
                src={imageUrl}
                alt={`${title}`}
                sx={{
                    width: 110,
                    height: 110,
                    borderRadius: 1,
                    objectFit: "cover",
                    flexShrink: 0,
                }}
            />

            <Typography
                variant="h5"
                noWrap
            >
                {title}
            </Typography>

            {subtitle && (
                <Typography
                    variant="body1"
                    noWrap
                >
                    {subtitle}
                </Typography>
            )}


            <Rating
                value={rating != null ? rating / 2 : null}
                onChange={(_, value) => {
                    setRating(value == null ? null : value * 2);
                }}
                precision={0.5}
                size= "large"
            />

            {ratingError && (
                <Typography
                    variant="caption"
                    color="error"
                >
                    {ratingError}
                </Typography>
            )}



            <TextField
                label = "Review"
                value = {reviewText}
                onChange={e => setReviewText(e.target.value)}
                error={Boolean(reviewTextError)}
                multiline
                slotProps={{
                    htmlInput: {
                        maxLength: MAX_REVIEW_LENGTH
                    }
                }}
                helperText={`${reviewText.length}/${MAX_REVIEW_LENGTH}`}
            />

            {submitError && (
                <Alert severity="error">
                    {submitError}
                </Alert>
            )}

            <Box
                sx={{
                    display: "flex",
                    justifyContent: "flex-end",
                    gap: 1.5,
                }}
            >
                <Button
                    onClick={onCancel}

                >
                    Cancel
                </Button>

                <Button
                    type="submit"
                    variant="contained"
                    sx = {{
                        bgcolor: "actionButton.main"
                    }}
                >
                    Submit
                </Button>


            </Box>
        </Box>
    )
}

export default ReviewForm;