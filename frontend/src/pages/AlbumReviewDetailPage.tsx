import {useParams} from "react-router-dom";
import {API_BASE_URL} from "../config/api.ts";
import type {AlbumReview} from "../types/AlbumReview.ts";
import DetailedAlbumReviewCard from "../components/cards/DetailedAlbumReviewCard.tsx";
import useReview from "../hooks/UseReview.tsx";
import {Alert, CircularProgress} from "@mui/material";

function AlbumReviewDetailPage(){
    const {id} = useParams();

    const {
        review: albumReview,
        updateReview,
        loading,
        error
    } = useReview<AlbumReview>(
        `${API_BASE_URL}/albums/reviews/${id}`,
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

    if (!albumReview) {
        return (
            <Alert severity="info">
                Review not found.
            </Alert>
        );
    }

    const handleAlbumUpdated = (updatedReview : AlbumReview) => {
        updateReview(updatedReview);
    }




    return(
        <div>
            <DetailedAlbumReviewCard
                albumReview={albumReview}
                onAlbumUpdated={handleAlbumUpdated}/>
        </div>
    )
}

export default AlbumReviewDetailPage;