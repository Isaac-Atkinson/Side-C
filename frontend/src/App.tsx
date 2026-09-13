import './App.css'
import AddAlbumForm from "./components/AddAlbumForm.tsx";
import AlbumList from "./components/AlbumList.tsx";

import { useEffect, useState } from "react";
import {API_BASE_URL} from "./config/api.ts";
import type {Album} from "./types/Album.ts";
import AddArtistForm from "./components/AddArtistForm.tsx";
import type {ArtistReview} from "./types/ArtistReview.ts";
import ArtistList from "./components/ArtistList.tsx";



function App() {
  const [albums, setAlbums] = useState<Album[]>([]);
  const[artistReviews, setArtistReviews] = useState<ArtistReview[]>([]);

  //Fetch all albums
  useEffect(() => {
    fetch(`${API_BASE_URL}/albums`,)
        .then(response => response.json())
        .then(data => setAlbums(data));
  }, [])

    useEffect(() => {
        fetch(`${API_BASE_URL}/artists/reviews`,)
            .then(response => response.json())
            .then(data => setArtistReviews(data));
    }, [])

  const handleAlbumDeleted = (id: number) => {
    setAlbums(albums.filter(album => album.id !== id));
  };





  return(

      <div>

        <h1>Music Tracker</h1>

        <AlbumList
            albums={albums}
            onAlbumDeleted={handleAlbumDeleted}
        />

        <ArtistList
            artistReviews={artistReviews}
        />

        <AddAlbumForm />
        <AddArtistForm />
      </div>

  )
}

export default App
