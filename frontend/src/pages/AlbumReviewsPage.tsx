import AlbumList from "../components/AlbumList.tsx";
import type {AlbumReview} from "../types/AlbumReview.ts";
import {useEffect, useState} from "react";
import {API_BASE_URL} from "../config/api.ts";
import AddAlbumForm from "../components/AddAlbumForm.tsx";





function AlbumReviewsPage() {
    const [albumReviews, setAlbumReviews] = useState<AlbumReview[]>([]);

    //Fetch all album reviews
    useEffect(() => {
        fetch(`${API_BASE_URL}/albums/reviews`,)
            .then(response => response.json())
            .then(data => setAlbumReviews(data));
    }, [])

    const handleAlbumDeleted = (id: number) => {
        setAlbumReviews(albumReviews.filter(albumReview => albumReview.id !== id));
    };

    const handleAlbumAdded = (newAlbumReview : AlbumReview) => {
        setAlbumReviews([...albumReviews, newAlbumReview]);
    }

    return (
        <div>
            <AlbumList
                albumReviews={albumReviews}
                onAlbumDeleted={handleAlbumDeleted}
        />
            <AddAlbumForm
                onAlbumAdded={handleAlbumAdded}
            />
        </div>

    );
}

export default AlbumReviewsPage