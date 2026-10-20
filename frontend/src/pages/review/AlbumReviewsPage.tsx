import type {AlbumReview} from "../../types/AlbumReview.ts";
import {API_BASE_URL} from "../../config/api.ts";
import {Alert, CircularProgress} from "@mui/material";
import useReviews from "../../hooks/UseReviews.tsx";
import {AlbumReviewCard} from "../../components/cards/ReviewCards.tsx";
import CardGrid from "../../components/layout/CardGrid.tsx";
import {useOutletContext} from "react-router-dom";
import {SORT_OPTIONS} from "../../components/layout/SortOptions.tsx";




type ReviewLayoutContext = {
    sortBy: string;
}

function AlbumReviewsPage() {

    const {sortBy} = useOutletContext<ReviewLayoutContext>()

    const {
        reviews : albumReviews,
        loading,
        error
    } = useReviews<AlbumReview>(
        `${API_BASE_URL}/albums/reviews`,
        "Could not load album reviews"
    )

    const sortedReviews = [...albumReviews].sort((a, b) => {
        switch (sortBy) {
           case SORT_OPTIONS.MOST_RECENT:
                return (new Date(b.created).getTime() -
                        new Date(a.created).getTime());
           case SORT_OPTIONS.OLDEST:
                return (new Date(a.created).getTime() -
                        new Date(b.created).getTime());
           case SORT_OPTIONS.ALPHABETICAL_AZ:
                return a.album.title.localeCompare(b.album.title);
           case SORT_OPTIONS.ALPHABETICAL_ZA:
                return b.album.title.localeCompare(a.album.title);
           case SORT_OPTIONS.HIGHEST_RATED:
                return b.rating - a.rating;
           case SORT_OPTIONS.LOWEST_RATED:
                return a.rating - b.rating;
           default:
               return 0;
    }});

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
                {sortedReviews.map(review => (
                    <AlbumReviewCard
                        key={review.id}
                        albumReview={review}
                    />
                ))}
            </CardGrid>
    );
}

export default AlbumReviewsPage