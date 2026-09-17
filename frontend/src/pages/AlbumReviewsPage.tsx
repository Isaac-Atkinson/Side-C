import AlbumList from "../components/AlbumList.tsx";
import type {AlbumReview} from "../types/AlbumReview.ts";
import {useEffect, useState} from "react";
import {API_BASE_URL} from "../config/api.ts";
import AddAlbumForm from "../components/forms/AddAlbumForm.tsx";
import {Button, Dialog, DialogContent, DialogTitle} from "@mui/material";
import {Link, useNavigate} from "react-router-dom";





function AlbumReviewsPage() {
    const [albumReviews, setAlbumReviews] = useState<AlbumReview[]>([]);

    const navigate = useNavigate();

    //Fetch all album reviews
    useEffect(() => {
        fetch(`${API_BASE_URL}/albums/reviews`,)
            .then(response => response.json())
            .then(data => setAlbumReviews(data));
    }, [])


    return (
        <div>
            <AlbumList
                albumReviews={albumReviews}
        />

            <Button component={Link} to="/albums/search" >
                Add New
            </Button>

            {/*<Dialog open = {addingNew} onClose = {() => navigate("/albums/search")} >*/}
            {/*    <DialogTitle>Add New Album Review</DialogTitle>*/}

            {/*    <DialogContent>*/}
            {/*        <AddAlbumForm*/}
            {/*            onAlbumAdded={handleAlbumAdded}*/}
            {/*            onSuccess = {() => setAddingNew(false)}*/}
            {/*            onCancel = {() => setAddingNew(false)}*/}
            {/*        />*/}
            {/*    </DialogContent>*/}
            {/*</Dialog>*/}

        </div>

    );
}

export default AlbumReviewsPage