import type {ArtistReview} from "../../types/ArtistReview.ts";
import {API_BASE_URL} from "../../config/api.ts";
import useReviews from "../../hooks/UseReviews.tsx";
import {Alert, CircularProgress} from "@mui/material";
import CardGrid from "../../components/CardGrid.tsx";
import {ArtistReviewCard} from "../../components/cards/ReviewCards.tsx";



function ArtistReviewsPage(){

    const {
        reviews : artistReviews,
        loading,
        error
    } = useReviews<ArtistReview>(
        `${API_BASE_URL}/artists/reviews`,
        "Could not load artist reviews"
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

    if (artistReviews.length === 0) {
        return (
            <Alert severity="info">
                You haven't reviewed any artists yet.
            </Alert>
        );
    }

    return (
        <div>
          <CardGrid>
              {artistReviews.map(review => (
                  <ArtistReviewCard
                      key = {review.id}
                      artistReview={review}
                  />
              ))}
          </CardGrid>

        </div>
    );
}

export default ArtistReviewsPage;