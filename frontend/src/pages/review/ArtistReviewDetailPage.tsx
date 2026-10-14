import {useParams} from "react-router-dom";
import {API_BASE_URL} from "../../config/api.ts";
import type {ArtistReview} from "../../types/ArtistReview.ts";
import DetailedArtistReviewCard from "../../components/cards/DetailedArtistReviewCard.tsx";
import useReview from "../../hooks/UseReview.tsx";
import {Alert, CircularProgress, Snackbar} from "@mui/material";
import {useState} from "react";

function ArtistReviewDetailPage() {
    const[snackBarOpen, setSnackBarOpen] = useState(false);
    const[snackBarMessage, setSnackBarMessage] = useState("");

    const {id} = useParams();

    const {
        review: artistReview,
        updateReview,
        loading,
        error
    } = useReview<ArtistReview>(
        `${API_BASE_URL}/artists/reviews/${id}`,
        "Error loading review",
    )

    if(loading) {
        return (
            <div className="loading-container">
                <CircularProgress/>
            </div>

        )
    }

    if (error) {
        return (
            <Alert severity="error">
                {error}
            </Alert>
        )
    }

    if (!artistReview) {
        return (
            <Alert severity="info">
                Review not found.
            </Alert>
        );
    }

    const handleArtistUpdated = (updatedReview : ArtistReview) => {
        updateReview(updatedReview);
        setSnackBarOpen(true);
        setSnackBarMessage("Review updated.");
    }
    return(
        <div>
            <DetailedArtistReviewCard
                artistReview={artistReview}
                onArtistUpdated={handleArtistUpdated}
            />

            <Snackbar
                open={snackBarOpen}
                onClose={() => setSnackBarOpen(false)}
                anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
            >
                <Alert
                    severity="success"
                    variant="filled"
                    onClose={() => setSnackBarOpen(false)}
                >
                    {snackBarMessage}
                </Alert>
            </Snackbar>
        </div>
    )
}

export default ArtistReviewDetailPage;