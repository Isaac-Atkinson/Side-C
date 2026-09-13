
import {Button} from "@mui/material";
import {API_BASE_URL} from "../config/api.ts";
import type {AlbumReview} from "../types/AlbumReview.ts";


type AlbumDataProps = {
    albumReview : AlbumReview;
    onAlbumDeleted: (id: number) => void;
};



function AlbumCard({albumReview, onAlbumDeleted }: AlbumDataProps){

    const handleDelete = async () => {
        const response = await fetch(`${API_BASE_URL}/albums/reviews/${albumReview.id}`,
            {
                method : "DELETE"
            }
        );

        if(response.ok){
            onAlbumDeleted(albumReview.id);
            console.log("Successfully deleted album");
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
        </div>
    );
}

export default AlbumCard;