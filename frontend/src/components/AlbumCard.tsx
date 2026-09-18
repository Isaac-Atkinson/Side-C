

import type {AlbumReview} from "../types/AlbumReview.ts";
import {Link} from "react-router-dom";


type AlbumDataProps = {
    albumReview : AlbumReview;
};



function AlbumCard({albumReview }: AlbumDataProps){


    return (
        <div>
            <img
                src = {albumReview.album.imageUrl}
                alt={`${albumReview.album.title} cover`}
                width = {150}/>
            <Link to={`/albums/${albumReview.id}`}>
                <h2>{albumReview.album.title}</h2>
            </Link>
            <p>{albumReview.album.artist.name}</p>
            <p>{albumReview.rating}</p>


        </div>
    );
}

export default AlbumCard;