import AlbumList from "../components/AlbumList.tsx";
import type {AlbumReview} from "../types/AlbumReview.ts";
import {useEffect, useState} from "react";
import {API_BASE_URL} from "../config/api.ts";
import AddAlbumForm from "../components/forms/AddAlbumForm.tsx";
import {Button, Dialog, DialogContent, DialogTitle} from "@mui/material";





function AlbumReviewsPage() {
    const [albumReviews, setAlbumReviews] = useState<AlbumReview[]>([]);
    const [addingNew, setAddingNew] = useState(false);

    //Fetch all album reviews
    useEffect(() => {
        fetch(`${API_BASE_URL}/albums/reviews`,)
            .then(response => response.json())
            .then(data => setAlbumReviews(data));
    }, [])


    const handleAlbumAdded = (newAlbumReview : AlbumReview) => {
        setAlbumReviews([...albumReviews, newAlbumReview]);
    }

    return (
        <div>
            <AlbumList
                albumReviews={albumReviews}
        />

            <Button onClick = {() => setAddingNew(true)} >
                Add New
            </Button>

            <Dialog open = {addingNew} onClose = {() => setAddingNew(false)} >
                <DialogTitle>Add New Album Review</DialogTitle>

                <DialogContent>
                    <AddAlbumForm
                        onAlbumAdded={handleAlbumAdded}
                        onSuccess = {() => setAddingNew(false)}
                        onCancel = {() => setAddingNew(false)}
                    />
                </DialogContent>
            </Dialog>

        </div>

    );
}

export default AlbumReviewsPage