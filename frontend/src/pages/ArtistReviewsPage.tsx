import {useEffect, useState} from "react";
import type {ArtistReview} from "../types/ArtistReview.ts";
import {API_BASE_URL} from "../config/api.ts";
import ArtistList from "../components/ArtistList.tsx";
import AddArtistForm from "../components/AddArtistForm.tsx";

function ArtistReviewsPage(){
    const[artistReviews, setArtistReviews] = useState<ArtistReview[]>([]);

    //Fetch all artist reviews
    useEffect(() => {
        fetch(`${API_BASE_URL}/artists/reviews`,)
            .then(response => response.json())
            .then(data => setArtistReviews(data));
    }, [])

    const handleArtistDeleted = (id: number) => {
        setArtistReviews(artistReviews.filter(artistReview => artistReview.id !== id));
    }



    const handleArtistAdded = (newArtistReview : ArtistReview) => {
        setArtistReviews([...artistReviews, newArtistReview]);
    }


    return (
        <div>
          <ArtistList
              artistReviews={artistReviews}
              onArtistDeleted={handleArtistDeleted}
          />

          <AddArtistForm
              onArtistAdded={handleArtistAdded}/>
        </div>
    );
}

export default ArtistReviewsPage;