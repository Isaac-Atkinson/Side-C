import {useState} from "react";
import { Box, Button, TextField } from "@mui/material";
import {API_BASE_URL} from "../config/api.ts";

function AddAlbumForm() {
    const[title, setTitle] = useState("");
    const[artist, setArtist] = useState("");
    const[year, setYear] = useState("");
    const[rating, setRating] = useState("");

    const handleSubmit = async (e: React.SubmitEvent) => {
        e.preventDefault();
        const response = await fetch(`${API_BASE_URL}/albums/reviews`, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({
                title: title,
                artist: artist,
                year: Number (year),
                rating: Number (rating),
            })
        });

        if(response.ok){
            console.log("Successfully added album");
        }

    }




    return (
        <Box
            component="form"
            onSubmit={handleSubmit}
        >
            <TextField
                label = "Title"
                value = {title}
                onChange={e => setTitle(e.target.value)}
            />

            <TextField
                label = "Artist"
                value = {artist}
                onChange={e => setArtist(e.target.value)}
            />

            <TextField
                label = "Year"
                type = "number"
                value = {year}
                onChange={e => setYear(e.target.value)}
            />

            <TextField
                label = "Rating"
                type = "number"
                value = {rating}
                onChange={e => setRating(e.target.value)}
            />

            <Button type="submit">
                Add Album
            </Button>
        </Box>
    );
}

export default AddAlbumForm;