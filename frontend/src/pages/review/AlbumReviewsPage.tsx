import type {AlbumReview} from "../../types/AlbumReview.ts";
import {API_BASE_URL} from "../../config/api.ts";
import {Alert, CircularProgress} from "@mui/material";
import useReviews from "../../hooks/UseReviews.tsx";
import {AlbumReviewCard} from "../../components/cards/ReviewCards.tsx";
import CardGrid from "../../components/layout/CardGrid.tsx";





function AlbumReviewsPage() {

    const {
        reviews : albumReviews,
        loading,
        error
    } = useReviews<AlbumReview>(
        `${API_BASE_URL}/albums/reviews`,
        "Could not load album reviews"
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

    if (albumReviews.length === 0) {
        return (
            <Alert severity="info">
                You haven't reviewed any albums yet.
            </Alert>
        );
    }


    return (
            <CardGrid>
                {albumReviews.map(review => (
                    <AlbumReviewCard
                        key={review.id}
                        albumReview={review}
                    />
                ))}
            </CardGrid>
    );
}

export default AlbumReviewsPage