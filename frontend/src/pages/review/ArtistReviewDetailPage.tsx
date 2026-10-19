import {useParams} from "react-router-dom";
import {API_BASE_URL} from "../../config/api.ts";
import type {ArtistReview} from "../../types/ArtistReview.ts";
import {DetailedArtistReviewCard} from "../../components/cards/DetailedReviewCardVariants.tsx";
import useReview from "../../hooks/UseReview.tsx";
import {Alert, CircularProgress} from "@mui/material";
import PageContainer from "../../components/layout/pageContainer.tsx";
import {useSnackbar} from "../../context/SnackBarContext.tsx";

function ArtistReviewDetailPage() {
    const {showSnackbar} = useSnackbar();

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
        showSnackbar("Review updated");
    }
    return(
        <PageContainer>
            <DetailedArtistReviewCard
                artistReview={artistReview}
                onArtistUpdated={handleArtistUpdated}
            />
        </PageContainer>
    )
}

export default ArtistReviewDetailPage;