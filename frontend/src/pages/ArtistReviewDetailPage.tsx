import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {API_BASE_URL} from "../config/api.ts";
import type {ArtistReview} from "../types/ArtistReview.ts";
import DetailedArtistReviewCard from "../components/cards/DetailedArtistReviewCard.tsx";

function ArtistReviewDetailPage() {

    const {id} = useParams();

    const [artistReview, setArtistReview] = useState<ArtistReview | null>(null);

    useEffect(()=>{
        fetch(`${API_BASE_URL}/artists/reviews/${id}`,
            {credentials: "include",})
            .then(res => res.json())
            .then(data => setArtistReview(data))
    }, [id])


    if (!artistReview) {
        return <p>Loading...</p>;
    }

    const handleArtistUpdated = (artistReview : ArtistReview) => {
        setArtistReview(artistReview);
    }
    return(
        <div>
            <DetailedArtistReviewCard
                artistReview={artistReview}
                onArtistUpdated={handleArtistUpdated}
            />
        </div>
    )
}

export default ArtistReviewDetailPage;