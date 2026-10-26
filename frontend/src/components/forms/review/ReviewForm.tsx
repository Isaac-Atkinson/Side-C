import {type ReactNode, useState} from "react";
import {Alert, Box, Button, Rating, TextField, Typography} from "@mui/material";
import {MAX_REVIEW_LENGTH} from "../../../config/api.ts";
import {DatePicker} from "@mui/x-date-pickers";
import dayjs, { type Dayjs } from "dayjs";

type ReviewFormProps = {
    title: string;
    subtitle?: string;

    existingRating?: number
    existingReviewText?: string;
    existingListenedOn?: string;
    showListenedOn?: boolean;

    imageUrl?: string;
    placeholder: ReactNode;

    loading: boolean;


    onSubmit: (
        rating : number,
        reviewText: string,
        listenedOn : string | null)
        => Promise<string | null>;

    onCancel: () => void;
}

function ReviewForm({title, subtitle, existingRating, existingReviewText, existingListenedOn, showListenedOn, imageUrl, placeholder, loading, onSubmit, onCancel}: ReviewFormProps) {
    const[rating, setRating] = useState<number | null>(existingRating ? existingRating : null);
    const[reviewText, setReviewText] = useState(existingReviewText ? existingReviewText : "");
    const[listenedOn, setListenedOn] = useState<Dayjs | null>(existingListenedOn ? dayjs(existingListenedOn) : dayjs());

    const [ratingError, setRatingError] = useState("");
    const [reviewTextError, setReviewTextError] = useState("");
    const [listenedOnError, setListenedOnError] = useState("");

    const [submitError, setSubmitError] = useState("");

    const validateForm = () => {
        let valid = true;

        setRatingError("");
        setReviewTextError("");
        setListenedOnError("");

        if (!rating) {
            setRatingError("Rating is required");
            valid = false;
        }

        if(showListenedOn && !listenedOn){
            setListenedOnError("Date is required");
            valid = false;
        }

        return valid;
    }

    const handleSubmit = async (e : React.SubmitEvent) => {
        e.preventDefault();

        if(!validateForm()) return;

        const listenedOnString = listenedOn
            ? listenedOn.format("YYYY-MM-DD")
            : null;

        const error = await onSubmit(Number (rating), reviewText, listenedOnString);

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
                gap: 2,
            }}
        >
            <Box
                sx = {{
                    display: "flex",
                    alignItems: "center",
                    gap: 2
                }}
            >

                {imageUrl ? (
                    <Box
                        component="img"
                        src={imageUrl}
                        alt={`${title}`}
                        sx={{
                            width: "30%",
                            height: "30%",
                            borderRadius: 1,
                            objectFit: "cover",
                            flexShrink: 0,
                        }}
                    />
                ) : (
                    <Box
                    sx={{
                        width: "30%",
                        aspectRatio: "1 / 1",
                        borderRadius: 1,
                        flexShrink: 0,
                        bgcolor: "action.hover",
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                    }}
                    >
                        {placeholder}
                    </Box>
                )}


                <Box
                    sx={{
                        display: "flex",
                        flexDirection: "column",
                        gap: 0,
                    }}
                >
                    <Typography
                        variant="h4"
                        noWrap
                    >
                        {title}
                    </Typography>

                    {subtitle && (
                        <Typography
                            variant="h5"
                            color={"textSecondary"}
                            noWrap
                        >
                            {subtitle}
                        </Typography>
                    )}

                </Box>
            </Box>




            <Box
                sx={{
                    display: "flex",
                    gap: 2

                }}
            >
                <Box
                    sx={{
                        display: "flex",
                        flexDirection: "column",
                        gap: 0.5,
                    }}
                >
                    <Typography
                        variant="body1"
                    >
                        Rating
                    </Typography>

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


                </Box>

                {showListenedOn && (
                    <Box sx={{ ml: "auto", mt: 1 }}>

                        <DatePicker
                            label="Listened on"
                            value={listenedOn}
                            onChange={(newValue) => setListenedOn(newValue)}
                            disableFuture
                            slotProps={{
                                textField: {
                                    error: Boolean(listenedOnError),
                                    helperText: listenedOnError,
                                },
                            }}
                        />
                    </Box>
                )}
            </Box>



            <Box
                sx={{
                    display: "flex",
                    flexDirection: "column",
                    gap: 1,
                }}
            >
                <Typography
                    variant="body1"
                >
                    Review
                </Typography>

                <TextField
                    value = {reviewText}
                    onChange={e => setReviewText(e.target.value)}
                    error={Boolean(reviewTextError)}
                    multiline
                    minRows={8}
                    slotProps={{
                        htmlInput: {
                            maxLength: MAX_REVIEW_LENGTH
                        }
                    }}
                    helperText={`${reviewText.length}/${MAX_REVIEW_LENGTH}`}
                />


            </Box>



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
                    sx = {{
                        color: "error.main"
                    }}
                >
                    Cancel
                </Button>

                <Button
                    type="submit"
                    variant="contained"
                    loading={loading}
                    sx = {{
                        bgcolor: "primary.main",
                    }}
                >
                    Submit
                </Button>


            </Box>
        </Box>
    )
}

export default ReviewForm;
