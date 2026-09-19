import type {AlbumReview} from "../../types/AlbumReview.ts";
import {Box, Button, Card, CardContent, CardMedia, Dialog, DialogContent, DialogTitle, Typography} from "@mui/material";
import {API_BASE_URL} from "../../config/api.ts";
import {Link, useNavigate} from "react-router-dom";
import {useState} from "react";
import EditAlbumForm from "../forms/EditAlbumForm.tsx";

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
                method : "DELETE"
            }
        );

        if(response.ok){
            console.log("Successfully deleted album");
           navigate("/albums");
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

                    <Typography
                        variant="h5"
                        sx={{ mt: 2 }}
                    >
                        {albumReview.rating}/10
                    </Typography>

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
            <Dialog open = {editing} onClose = {() => setEditing(false)} >
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
        // <div>
        //     <h2>{albumReview.album.title}</h2>
        //     <p>{albumReview.album.artist.name}</p>
        //     <p>{albumReview.rating}</p>
        //     <p>{albumReview.reviewText}</p>
        //
        //     <Button onClick={handleDelete}>
        //         Delete
        //     </Button>
        //
        //     <Button onClick ={() => {
        //         setEditing(true);
        //         }}>
        //             Edit
        //     </Button>
        //

        // </div>
    )
}

export default DetailedAlbumReviewCard;