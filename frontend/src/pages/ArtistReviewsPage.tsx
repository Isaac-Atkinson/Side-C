import {useEffect, useState} from "react";
import type {ArtistReview} from "../types/ArtistReview.ts";
import {API_BASE_URL} from "../config/api.ts";
import ArtistList from "../components/ArtistList.tsx";
import {Button} from "@mui/material";
import {Link} from "react-router-dom";


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

          <Button component={Link} to="/search" >
              Add New
          </Button>


        </div>
    );
}

export default ArtistReviewsPage;