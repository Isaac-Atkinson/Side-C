import {useNavigate} from "react-router-dom";
import { useState} from "react";
import {API_BASE_URL} from "../../config/api.ts";
import type {ArtistReview} from "../../types/ArtistReview.ts";
import {Box, Button, Dialog, DialogContent, DialogTitle, Rating, Typography} from "@mui/material";
import {EditArtistForm} from "../forms/ReviewFormVariants.tsx";


type DetailedArtistCardProps = {
    artistReview: ArtistReview;
    onArtistUpdated: (artistReview: ArtistReview) => void;
}
function DetailedArtistReviewCard({artistReview, onArtistUpdated}: DetailedArtistCardProps) {
    const navigate = useNavigate();
    const [editing, setEditing] = useState(false);

    const handleDelete = async () => {


        const response = await fetch(`${API_BASE_URL}/artists/reviews/${artistReview.id}`,
            {
                method : "DELETE",
                credentials: "include",
            }
        );

        if(response.ok){
            console.log("Successfully deleted artist");
            navigate("/artists");
        } else if(response.status === 404){
            console.log("Artist not found");
        } else {
            console.log("Error while deleting artist");
        }
    }
    return (
        <Box
            sx={{
                maxWidth: 900,
                mx: "auto",
                p: 3
            }}
        >
            <Box
                sx={{
                    display: "flex",
                    gap: 4,
                    alignItems: "flex-start"
                }}
            >
                <Box
                    component="img"
                    src={artistReview.artist.imageUrl}
                    alt={`${artistReview.artist.name} image`}
                    sx={{
                        width: 280,
                        borderRadius: 2
                    }}
                />

                <Box sx={{ flex: 1 }}>
                    <Typography variant="h3">
                        {artistReview.artist.name}
                    </Typography>


                    <Rating
                        value={Number(artistReview.rating) / 2}
                        precision={0.5}
                        size= "large"
                    />

                    <Typography
                        variant="body1"
                        sx={{
                            lineHeight: 1.8,
                            whiteSpace: "pre-line"
                        }}
                    >
                        {artistReview.reviewText}
                    </Typography>

                    <Box sx={{ display: "flex", gap: 1, mt: 4 }}>
                        <Button variant="contained" onClick ={() => {
                            setEditing(true);}}>
                            Edit
                        </Button>

                        <Button variant="outlined" color="error" onClick={handleDelete}>
                            Delete
                        </Button>
                    </Box>
                </Box>
            </Box>
            <Dialog open = {editing} onClose = {() => setEditing(false)} >
                <DialogTitle>Edit Review</DialogTitle>

                <DialogContent>
                    <EditArtistForm
                        artistReview = {artistReview}
                        onCancel={() => setEditing(false)}
                        onArtistUpdated = {onArtistUpdated}
                        onSuccess = {() => setEditing(false)}
                    />
                </DialogContent>


            </Dialog>
        </Box>
    )
}



export default DetailedArtistReviewCard;