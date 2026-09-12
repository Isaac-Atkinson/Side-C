import type {Album} from "../types/Album.ts";
import {Button} from "@mui/material";
import {API_BASE_URL} from "../config/api.ts";


type AlbumDataProps = {
    album : Album;
    onAlbumDeleted: (id: number) => void;
};



function AlbumCard({album, onAlbumDeleted }: AlbumDataProps){

    const handleDelete = async () => {
        const response = await fetch(`${API_BASE_URL}/albums/${album.id}`,
            {
                method : "DELETE"
            }
        );

        if(response.ok){
            onAlbumDeleted(album.id);
            console.log("Successfully deleted album");
        } else {
            console.log("Error while deleting album");
        }
    }


    return (
        <div>
            <h2>{album.title}</h2>
            <p>{album.year}</p>
            <p>{album.artist.name}</p>
            <p>{album.rating}</p>

            <Button onClick={handleDelete}>
                Delete
            </Button>
        </div>
    );
}

export default AlbumCard;