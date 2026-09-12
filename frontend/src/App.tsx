import './App.css'
import AddAlbumForm from "./components/AddAlbumForm.tsx";

import { useEffect, useState } from "react";
import {API_BASE_URL} from "./config/api.ts";

type Artist = {
  id: number
  name: string,
};

type Album = {
  id: number,
  title: string,
  year: number,
  rating: number,
  artist: Artist
};


function App() {
  const [albums, setAlbums] = useState<Album[]>([]);

  //Fetch all albums
  useEffect(() => {
    fetch(`${API_BASE_URL}/albums`,)
        .then(response => response.json())
        .then(data => setAlbums(data));
  }, [])



  return (

      <div>
        <h1>music-tracker</h1>


        {albums.map(album => (
            <div key={album.id}>
              <h2>{album.title}</h2>
              <p>{album.year}</p>
              <p>{album.artist.name}</p>
              <p>{album.rating}</p>
            </div>
        ))}

        <AddAlbumForm />
      </div>


  );
}

export default App
