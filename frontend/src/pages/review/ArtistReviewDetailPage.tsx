import {useParams} from "react-router-dom";
import {API_BASE_URL} from "../../config/api.ts";
import type {ArtistReview} from "../../types/ArtistReview.ts";
import DetailedArtistReviewCard from "../../components/cards/DetailedArtistReviewCard.tsx";
import useReview from "../../hooks/UseReview.tsx";
import {Alert, CircularProgress} from "@mui/material";

function ArtistReviewDetailPage() {

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
    }
    return(
        <div>
            <DetailedArtistReviewCard
                artistReview={artistReview}
                onArtistUpdated={handleArtistUpdated}
            />
        </div>
    )
}

export default ArtistReviewDetailPage;