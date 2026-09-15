import './App.css'
import AlbumReviewsPage from "./pages/AlbumReviewsPage.tsx";
import ArtistReviewsPage from "./pages/ArtistReviewsPage.tsx";
import {Route, Routes} from "react-router-dom";
import { Link } from "react-router-dom";
import AlbumReviewDetailPage from "./pages/AlbumReviewDetailPage.tsx";


function App() {

  return(
      <>
          <nav>
              <Link to="/albums">Albums</Link>
              {" | "}
              <Link to="/artists">Artists</Link>
          </nav>

          <Routes>
              <Route path="/albums" element={<AlbumReviewsPage />} />
              <Route path="/artists" element={<ArtistReviewsPage />} />
              <Route path={"albums/:id"} element={<AlbumReviewDetailPage />}/>
          </Routes>
      </>
  )
}

export default App
