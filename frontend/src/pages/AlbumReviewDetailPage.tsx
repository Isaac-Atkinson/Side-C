import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {API_BASE_URL} from "../config/api.ts";
import type {AlbumReview} from "../types/AlbumReview.ts";
import DetailedAlbumCard from "../components/cards/DetailedAlbumCard.tsx";

function AlbumReviewDetailPage(){
    const {id} = useParams();

    const [albumReview, setAlbumReview] = useState<AlbumReview | null>(null);

    useEffect(()=>{
        fetch(`${API_BASE_URL}/albums/reviews/${id}`)
            .then(res => res.json())
            .then(data => setAlbumReview(data))
    }, [id])


    if (!albumReview) {
        return <p>Loading...</p>;
    }

    const handleAlbumUpdated = (albumReview : AlbumReview) => {
        setAlbumReview(albumReview);
    }




    return(
        <div>
            <DetailedAlbumCard
                albumReview={albumReview}
                onAlbumUpdated={handleAlbumUpdated}/>
        </div>
    )
}

export default AlbumReviewDetailPage;