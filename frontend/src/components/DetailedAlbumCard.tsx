import type {AlbumReview} from "../types/AlbumReview.ts";
import {Button} from "@mui/material";
import {API_BASE_URL} from "../config/api.ts";
import {useNavigate} from "react-router-dom";
import {useState} from "react";
import EditAlbumForm from "./forms/EditAlbumForm.tsx";

type DetailedAlbumCardProps = {
    albumReview: AlbumReview;
    onAlbumUpdated: (albumReview: AlbumReview) => void;
}

function DetailedAlbumCard({albumReview, onAlbumUpdated}: DetailedAlbumCardProps){
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
        } else {
            console.log("Error while deleting album");
        }
    }


    return (
        <div>
            <h2>{albumReview.album.title}</h2>
            <p>{albumReview.album.artist.name}</p>
            <p>{albumReview.rating}</p>

            <Button onClick={handleDelete}>
                Delete
            </Button>

            <Button onClick ={() => {
                setEditing(true);
                }}>
                    Edit
            </Button>

            {editing && (
                <EditAlbumForm albumReview={albumReview}
                               onCancel={() => setEditing(false)}
                               onAlbumUpdated={onAlbumUpdated}
                               />
            )}
        </div>
    )
}

export default DetailedAlbumCard;