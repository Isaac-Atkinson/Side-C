import {Box, Button, TextField} from "@mui/material";
import {useState} from "react";
import {API_BASE_URL} from "../../config/api.ts";
import type {AlbumReview} from "../../types/AlbumReview.ts";

type EditAlbumFormProps = {
    albumReview: AlbumReview;
    onCancel: () => void;
    onAlbumUpdated: (albumReview : AlbumReview) => void;
}
function EditAlbumForm({albumReview, onCancel, onAlbumUpdated}: EditAlbumFormProps) {
    const[rating, setRating] = useState("");

    const handleSubmit = async (e: React.SubmitEvent) => {
        e.preventDefault();
        const response = await fetch(`${API_BASE_URL}/albums/reviews/${albumReview.id}`, {
            method: "PUT",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({
                title: albumReview.album.title,
                artist: albumReview.album.artist.name,
                year: Number(albumReview.album.year),
                rating: Number(rating),
            })
        });
        if (response.ok) {
            const updatedAlbumReview: AlbumReview = await response.json();
            onAlbumUpdated(updatedAlbumReview);

            console.log("Successfully updated album");
        }
    }

    return (
        <div>
            <Box
                component="form"
                onSubmit={handleSubmit}
            >
                <TextField
                    label = "Rating"
                    type = "number"
                    value = {rating}
                    onChange={e => setRating(e.target.value)}
                />

                <Button type="submit">
                    Submit
                </Button>

                <Button onClick={onCancel}>
                    Cancel
                </Button>
            </Box>
        </div>
    )
}

export default EditAlbumForm;