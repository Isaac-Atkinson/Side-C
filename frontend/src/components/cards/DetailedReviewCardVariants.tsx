import type {AlbumReview} from "../../types/AlbumReview.ts";
import DetailedReviewCard from "./DetailedReviewCard.tsx";
import {API_BASE_URL} from "../../config/api.ts";
import {useNavigate} from "react-router-dom";
import type {ArtistReview} from "../../types/ArtistReview.ts";
import {useState} from "react";
import {EditAlbumForm, EditArtistForm} from "../forms/review/ReviewFormVariants.tsx";
import {Dialog, DialogContent, DialogTitle} from "@mui/material";
import {AlbumPlaceholder, ArtistPlaceholder} from "../placeholders/placeholders.tsx";
import {apiFetch} from "../../api/ApiFetch.ts";
import {useSnackbar} from "../../context/SnackBarContext.tsx";

type DetailedAlbumCardProps = {
    albumReview: AlbumReview;
    onAlbumUpdated: (albumReview: AlbumReview) => void;
}

export function DetailedAlbumReviewCard({albumReview, onAlbumUpdated}: DetailedAlbumCardProps) {

    const [editing, setEditing] = useState(false);
    const navigate = useNavigate();
    const {showSnackbar} = useSnackbar();

    const handleDelete = async () => {


        const response = await apiFetch(`${API_BASE_URL}/albums/reviews/${albumReview.id}`,
            {
                method : "DELETE",
            }
        );

        if(response.ok){
            showSnackbar("Album review deleted");
            navigate("/reviews/albums");

        }
    }

    return (
        <>
            <DetailedReviewCard
                title={albumReview.album.title}
                subtitle={albumReview.album.artist.name}

                rating={albumReview.rating}
                reviewText={albumReview.reviewText}
                listenedOn={albumReview.listenedOn}
                showListenedOn={true}

                imageUrl={albumReview.album.imageUrl}
                imageAlt={`${albumReview.album.title} cover`}
                placeholder={AlbumPlaceholder}

                onDelete={handleDelete}
                onEdit={() => setEditing(true)}
            />

            <Dialog
                open = {editing}
                onClose = {() => setEditing(false)}
                fullWidth
            >
                <DialogTitle>Edit Review</DialogTitle>

                <DialogContent>
                    <EditAlbumForm
                        albumReview={albumReview}
                        onSuccess={() => setEditing(false)}
                        onCancel={() => setEditing(false)}
                        onAlbumUpdated={onAlbumUpdated}
                    />
                </DialogContent>
            </Dialog>
        </>



    )
}

type DetailedArtistCardProps = {
    artistReview: ArtistReview;
    onArtistUpdated: (artistReview: ArtistReview) => void;
}

export function DetailedArtistReviewCard({artistReview, onArtistUpdated}: DetailedArtistCardProps) {
    const [editing, setEditing] = useState(false);
    const navigate = useNavigate();
    const {showSnackbar} = useSnackbar();

    const handleDelete = async () => {


        const response = await apiFetch(`${API_BASE_URL}/artists/reviews/${artistReview.id}`,
            {
                method : "DELETE",
            }
        );

        if(response.ok){
            showSnackbar("Artist review deleted");
            navigate("/reviews/artists");
        }
    }

    return (
        <>
        <DetailedReviewCard
            title={artistReview.artist.name}

            rating={artistReview.rating}
            reviewText={artistReview.reviewText}
            showListenedOn={false}

            imageUrl={artistReview.artist.imageUrl}
            imageAlt={`${artistReview.artist.name}`}
            placeholder={ArtistPlaceholder}

            onDelete={handleDelete}
            onEdit={() => setEditing(true)}
        />

        <Dialog
            open = {editing}
            onClose = {() => setEditing(false)}
            fullWidth
        >
            <DialogTitle>Edit Review</DialogTitle>

            <DialogContent>
                <EditArtistForm
                    artistReview={artistReview}
                    onSuccess={() => setEditing(false)}
                    onCancel={() => setEditing(false)}
                    onArtistUpdated={onArtistUpdated}
                />
            </DialogContent>
        </Dialog>
        </>
    )
}