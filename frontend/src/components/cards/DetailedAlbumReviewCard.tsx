import type {AlbumReview} from "../../types/AlbumReview.ts";
import {Box, Button, Dialog, DialogContent, DialogTitle, Rating, Typography} from "@mui/material";
import {API_BASE_URL} from "../../config/api.ts";
import {useNavigate} from "react-router-dom";
import {useState} from "react";
import {EditAlbumForm} from "../forms/review/ReviewFormVariants.tsx";

type DetailedAlbumCardProps = {
    albumReview: AlbumReview;
    onAlbumUpdated: (albumReview: AlbumReview) => void;

}

function DetailedAlbumReviewCard({albumReview, onAlbumUpdated}: DetailedAlbumCardProps){
    const navigate = useNavigate();
    const [editing, setEditing] = useState(false);

    const handleDelete = async () => {


        const response = await fetch(`${API_BASE_URL}/albums/reviews/${albumReview.id}`,
            {
                method : "DELETE",
                credentials: "include",
            }
        );

        if(response.ok){
           navigate("/reviews/albums");
        } else if (response.status === 404) {
            console.log("Album not found");
        } else {
            console.log("Error while deleting album");
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
                    src={albumReview.album.imageUrl}
                    alt={`${albumReview.album.title} cover`}
                    sx={{
                        width: 280,
                        borderRadius: 2
                    }}
                />

                <Box sx={{ flex: 1 }}>
                    <Typography variant="h3">
                        {albumReview.album.title}
                    </Typography>

                    <Typography
                        variant="h6"
                        sx={{ color: "#a7a7a7", mt: 1 }}
                    >
                        {albumReview.album.artist.name}
                    </Typography>

                    <Rating
                        value={Number(albumReview.rating) / 2}
                        precision={0.5}
                        size= "large"
                        readOnly
                    />

                    <Typography
                        variant="body1"
                        sx={{
                            lineHeight: 1.8,
                            whiteSpace: "pre-line"
                        }}
                    >
                        {albumReview.reviewText}
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
            <Dialog
                open = {editing}
                onClose = {() => setEditing(false)}
                fullWidth
            >
                <DialogTitle>Edit Review</DialogTitle>

                <DialogContent>
                    <EditAlbumForm albumReview={albumReview}
                                   onCancel={() => setEditing(false)}
                                   onAlbumUpdated={onAlbumUpdated}
                                   onSuccess = {() => setEditing(false)}
                    />
                </DialogContent>


            </Dialog>
        </Box>

    )
}

export default DetailedAlbumReviewCard;