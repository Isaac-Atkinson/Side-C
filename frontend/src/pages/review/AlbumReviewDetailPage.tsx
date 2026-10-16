import {useParams} from "react-router-dom";
import {API_BASE_URL} from "../../config/api.ts";
import type {AlbumReview} from "../../types/AlbumReview.ts";
import DetailedAlbumReviewCard from "../../components/cards/DetailedAlbumReviewCard.tsx";
import useReview from "../../hooks/UseReview.tsx";
import {Alert, CircularProgress, Snackbar} from "@mui/material";
import {useState} from "react";
import PageContainer from "../../components/layout/pageContainer.tsx";

function AlbumReviewDetailPage(){
    const[snackBarOpen, setSnackBarOpen] = useState(false);
    const[snackBarMessage, setSnackBarMessage] = useState("");

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
        setSnackBarOpen(true);
        setSnackBarMessage("Review updated");
    }




    return(
        <PageContainer>
            <DetailedAlbumReviewCard
                albumReview={albumReview}
                onAlbumUpdated={handleAlbumUpdated}/>

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
        </PageContainer>
    )
}

export default AlbumReviewDetailPage;