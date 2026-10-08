import {useEffect, useState} from "react";
import type {ArtistReview} from "../types/ArtistReview.ts";
import {API_BASE_URL} from "../config/api.ts";
import ArtistList from "../components/ArtistList.tsx";



function ArtistReviewsPage(){
    const[artistReviews, setArtistReviews] = useState<ArtistReview[]>([]);


    //Fetch all artist reviews
    useEffect(() => {
        fetch(`${API_BASE_URL}/artists/reviews`,{
            credentials: "include",
        })
            .then(response => response.json())
            .then(data => setArtistReviews(data));
    }, [])

    return (
        <div>
          <ArtistList
              artistReviews={artistReviews}
          />

        </div>
    );
}

export default ArtistReviewsPage;