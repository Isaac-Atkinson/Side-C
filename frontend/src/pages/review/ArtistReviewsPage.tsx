import type {ArtistReview} from "../../types/ArtistReview.ts";
import {API_BASE_URL} from "../../config/api.ts";
import useReviews from "../../hooks/UseReviews.tsx";
import {Alert, CircularProgress} from "@mui/material";
import CardGrid from "../../components/layout/CardGrid.tsx";
import {ArtistReviewCard} from "../../components/cards/ReviewCards.tsx";
import {useOutletContext} from "react-router-dom";
import {SORT_OPTIONS} from "../../components/layout/SortOptions.tsx";

type ReviewLayoutContext = {
    sortBy: string;
}



function ArtistReviewsPage(){

    const {sortBy} = useOutletContext<ReviewLayoutContext>()

    const {
        reviews : artistReviews,
        loading,
        error
    } = useReviews<ArtistReview>(
        `${API_BASE_URL}/artists/reviews`,
        "Could not load artist reviews"
    )

    const sortedReviews = [...artistReviews].sort((a, b) => {
        switch (sortBy) {
            case SORT_OPTIONS.CREATED_NEWEST:
                return (new Date(b.created).getTime() -
                    new Date(a.created).getTime());
            case SORT_OPTIONS.CREATED_OLDEST:
                return (new Date(a.created).getTime() -
                    new Date(b.created).getTime());
            case SORT_OPTIONS.ALPHABETICAL_AZ:
                return a.artist.name.localeCompare(b.artist.name);
            case SORT_OPTIONS.ALPHABETICAL_ZA:
                return b.artist.name.localeCompare(a.artist.name);
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

    if (artistReviews.length === 0) {
        return (
            <Alert severity="info">
                You haven't reviewed any artists yet.
            </Alert>
        );
    }

    return (
            <CardGrid>
                {sortedReviews.map(review => (
                    <ArtistReviewCard
                        key = {review.id}
                        artistReview={review}
                    />
                ))}
            </CardGrid>
    );
}

export default ArtistReviewsPage;