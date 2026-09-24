import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {API_BASE_URL} from "../config/api.ts";
import type {AlbumReview} from "../types/AlbumReview.ts";
import DetailedAlbumReviewCard from "../components/cards/DetailedAlbumReviewCard.tsx";

function AlbumReviewDetailPage(){
    const {id} = useParams();

    const [albumReview, setAlbumReview] = useState<AlbumReview | null>(null);

    useEffect(()=>{
        fetch(`${API_BASE_URL}/albums/reviews/${id}`,{
            credentials: "include",
        })
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
            <DetailedAlbumReviewCard
                albumReview={albumReview}
                onAlbumUpdated={handleAlbumUpdated}/>
        </div>
    )
}

export default AlbumReviewDetailPage;