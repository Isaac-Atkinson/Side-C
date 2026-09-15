import {useEffect, useState} from "react";
import type {ArtistReview} from "../types/ArtistReview.ts";
import {API_BASE_URL} from "../config/api.ts";
import ArtistList from "../components/ArtistList.tsx";
import AddArtistForm from "../components/forms/AddArtistForm.tsx";
import {Button, Dialog, DialogContent, DialogTitle} from "@mui/material";


function ArtistReviewsPage(){
    const[artistReviews, setArtistReviews] = useState<ArtistReview[]>([]);
    const[addingNew, setAddingNew] = useState<boolean>(false);

    //Fetch all artist reviews
    useEffect(() => {
        fetch(`${API_BASE_URL}/artists/reviews`,)
            .then(response => response.json())
            .then(data => setArtistReviews(data));
    }, [])


    const handleArtistAdded = (newArtistReview : ArtistReview) => {
        setArtistReviews([...artistReviews, newArtistReview]);
    }


    return (
        <div>
          <ArtistList
              artistReviews={artistReviews}
          />
          <Button onClick = {() => setAddingNew(true)}>
              Add New
          </Button>

            <Dialog open = {addingNew} onClose = {() => setAddingNew(false)} >
                <DialogTitle>Add New Artist Review</DialogTitle>

                <DialogContent>
                    <AddArtistForm
                        onArtistAdded={handleArtistAdded}
                        onSuccess = {() => setAddingNew(false)}
                        onCancel = {() => setAddingNew(false)}
                    />
                </DialogContent>
            </Dialog>


        </div>
    );
}

export default ArtistReviewsPage;