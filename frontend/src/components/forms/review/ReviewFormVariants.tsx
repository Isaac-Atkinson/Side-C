import {API_BASE_URL} from "../../../config/api.ts";
import ReviewForm from "./ReviewForm.tsx";
import type {AlbumSearchResult} from "../../../types/AlbumSearchResult.ts";
import type {ArtistSearchResult} from "../../../types/ArtistSearchResult.ts";
import type {AlbumReview} from "../../../types/AlbumReview.ts";
import type {ArtistReview} from "../../../types/ArtistReview.ts";
import {AlbumPlaceholder, ArtistPlaceholder} from "../../placeholders/placeholders.tsx";
import {apiFetch} from "../../../api/ApiFetch.ts";
import {useState} from "react";


type AddAlbumFormProps = {
    album : AlbumSearchResult
    onSuccess: () => void;
    onCancel: () => void;
}

export function AddAlbumForm({album, onSuccess, onCancel} : AddAlbumFormProps) {

    const[loading, setLoading] = useState(false);

    const submit = async (rating : number, reviewText : string, listenedOn : string | null) => {

        try{

            setLoading(true);

            const response = await apiFetch(`${API_BASE_URL}/albums/reviews`, {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({
                    musicBrainzId: album.musicBrainzId,
                    lastFMUrl: album.lastFMUrl,
                    title: album.title,
                    artist: album.artist,
                    rating: rating,
                    reviewText: reviewText,
                    listenedOn: listenedOn
                })
            });

            if(response.ok){

                onSuccess();
                return null;
            } else if(response.status === 409){
                return "You have already reviewed this album";
            } else if(response.status === 400){
                return "Invalid information entered";
        }

            return "Something went wrong";
        } catch {
            return "Something went wrong";
        } finally {
            setLoading(false);
        }


    }

    return (
        <ReviewForm
            title={album.title}
            subtitle={album.artist}

            imageUrl={album.imageUrl}
            placeholder={AlbumPlaceholder}

            showListenedOn={true}

            loading={loading}

            onSubmit={submit}
            onCancel={onCancel}

        />
    )
}

type AddArtistFormProps = {
    artist : ArtistSearchResult
    onSuccess: () => void;
    onCancel: () => void;
}

export function AddArtistForm({artist, onSuccess, onCancel} : AddArtistFormProps) {

    const[loading, setLoading] = useState(false);

    const submit = async (rating : number, reviewText : string) => {


        try{
            setLoading(true);

            const response = await apiFetch(`${API_BASE_URL}/artists/reviews`, {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({
                    musicBrainzId : artist.musicBrainzId,
                    lastFMUrl: artist.lastFMUrl,
                    name: artist.name,
                    imageUrl: artist.imageUrl,
                    rating: rating,
                    reviewText: reviewText,
                })
            });

            if(response.ok){

                onSuccess();
                return null;
            } else if(response.status === 409){
                return "You have already reviewed this artist";
            } else if(response.status === 400){
                return "Invalid information entered";
            }

            return "Something went wrong";
        } catch {
            return "Something went wrong";
        } finally {
            setLoading(false);
        }


    }

    return (
        <ReviewForm
            title={artist.name}
            imageUrl={artist.imageUrl}

            placeholder={ArtistPlaceholder}

            loading={loading}

            onSubmit={submit}
            onCancel={onCancel}
        />
    )
}

type EditAlbumFormProps = {
    albumReview: AlbumReview;
    onAlbumUpdated: (albumReview : AlbumReview) => void;
    onSuccess: () => void;
    onCancel: () => void;
}

export function EditAlbumForm({albumReview, onAlbumUpdated, onSuccess, onCancel}: EditAlbumFormProps) {

    const[loading, setLoading] = useState(false);

    const submit = async (rating : number, reviewText : string, listenedOn : string | null) => {

        try{
            setLoading(true);

            const response = await apiFetch(`${API_BASE_URL}/albums/reviews/${albumReview.id}`, {
                method: "PUT",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({
                    rating: rating,
                    reviewText: reviewText,
                    listenedOn: listenedOn,
                })
            });

            if(response.ok){

                const updatedAlbumReview: AlbumReview = await response.json();
                onAlbumUpdated(updatedAlbumReview);

                onSuccess();
                return null;
            } else if(response.status === 404){
                return "Review not found";
            }

            return "Something went wrong";
        } catch {
            return "Something went wrong";
        } finally {
            setLoading(false);
        }



    }

    return (
        <ReviewForm
            title={albumReview.album.title}
            subtitle={albumReview.album.artist.name}

            existingRating={albumReview.rating}
            existingReviewText={albumReview.reviewText}
            existingListenedOn={albumReview.listenedOn}
            showListenedOn={true}

            imageUrl={albumReview.album.imageUrl}
            placeholder={AlbumPlaceholder}

            loading={loading}

            onSubmit={submit}
            onCancel={onCancel}
        />
    )
}

type EditArtistFormProps = {
    artistReview: ArtistReview;
    onArtistUpdated: (artistReview: ArtistReview) => void;
    onSuccess : () => void;
    onCancel: () => void;
}

export function EditArtistForm({artistReview, onArtistUpdated, onSuccess, onCancel}: EditArtistFormProps) {

    const[loading, setLoading] = useState(false);

    const submit = async (rating : number, reviewText : string) => {



        try{
            setLoading(true);

            const response = await apiFetch(`${API_BASE_URL}/artists/reviews/${artistReview.id}`, {
                method: "PUT",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({
                    rating: rating,
                    reviewText: reviewText,

                })
            });

            if(response.ok){

                const updatedArtistReview: ArtistReview = await response.json();
                onArtistUpdated(updatedArtistReview);

                onSuccess();
                return null;
            } else if(response.status === 404){
                return "Review not found";
            }

            return "Something went wrong";
        } catch {
            return "Something went wrong";
        } finally {
            setLoading(false);
        }



    }

    return (
        <ReviewForm
            title={artistReview.artist.name}

            existingRating={artistReview.rating}
            existingReviewText={artistReview.reviewText}

            imageUrl={artistReview.artist.imageUrl}
            placeholder={ArtistPlaceholder}

            loading={loading}

            onSubmit={submit}
            onCancel={onCancel}
        />
    )
}