import AlbumList from "../components/AlbumList.tsx";
import type {AlbumReview} from "../types/AlbumReview.ts";
import {useEffect, useState} from "react";
import {API_BASE_URL} from "../config/api.ts";




function AlbumReviewsPage() {
    const [albumReviews, setAlbumReviews] = useState<AlbumReview[]>([]);


    //Fetch all album reviews
    useEffect(() => {
        fetch(`${API_BASE_URL}/albums/reviews`,{
            credentials: "include",
        })
            .then(response => response.json())
            .then(data => setAlbumReviews(data));
    }, [])


    return (
        <div>

            <AlbumList
                albumReviews={albumReviews}
        />

        </div>

    );
}

export default AlbumReviewsPage