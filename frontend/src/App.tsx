import './App.css'
import AddAlbumForm from "./components/AddAlbumForm.tsx";
import AlbumList from "./components/AlbumList.tsx";

import { useEffect, useState } from "react";
import {API_BASE_URL} from "./config/api.ts";

import AddArtistForm from "./components/AddArtistForm.tsx";
import type {ArtistReview} from "./types/ArtistReview.ts";
import ArtistList from "./components/ArtistList.tsx";
import type {AlbumReview} from "./types/AlbumReview.ts";



function App() {
  const [albumReviews, setAlbumReviews] = useState<AlbumReview[]>([]);
  const[artistReviews, setArtistReviews] = useState<ArtistReview[]>([]);

  //Fetch all albums
  useEffect(() => {
    fetch(`${API_BASE_URL}/albums/reviews`,)
        .then(response => response.json())
        .then(data => setAlbumReviews(data));
  }, [])

    useEffect(() => {
        fetch(`${API_BASE_URL}/artists/reviews`,)
            .then(response => response.json())
            .then(data => setArtistReviews(data));
    }, [])

  const handleAlbumDeleted = (id: number) => {
    setAlbumReviews(albumReviews.filter(albumReview => albumReview.id !== id));
  };

  const handleArtistDeleted = (id: number) => {
      setArtistReviews(artistReviews.filter(artistReview => artistReview.id !== id));
  }

  const handleAlbumAdded = (newAlbumReview : AlbumReview) => {
      setAlbumReviews([...albumReviews, newAlbumReview]);
  }

  const handleArtistAdded = (newArtistReview : ArtistReview) => {
      setArtistReviews([...artistReviews, newArtistReview]);
  }


  return(

      <div>

        <h1>Music Tracker</h1>

        <AlbumList
            albumReviews={albumReviews}
            onAlbumDeleted={handleAlbumDeleted}
        />

        <ArtistList
            artistReviews={artistReviews}
            onArtistDeleted={handleArtistDeleted}
        />

        <AddAlbumForm
            onAlbumAdded={handleAlbumAdded}/>
        <AddArtistForm
            onArtistAdded={handleArtistAdded}/>
      </div>

  )
}

export default App
