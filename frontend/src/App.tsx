
import heroImg from './assets/hero.png'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import './App.css'

import { useEffect, useState } from "react";

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

  useEffect(() => {
    fetch("http://localhost:8080/api/albums")
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
      </div>
  );
}

export default App
