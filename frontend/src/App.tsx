import './App.css'
import AlbumReviewsPage from "./pages/AlbumReviewsPage.tsx";
import ArtistReviewsPage from "./pages/ArtistReviewsPage.tsx";
import {Route, Routes} from "react-router-dom";
import { Link } from "react-router-dom";
import AlbumReviewDetailPage from "./pages/AlbumReviewDetailPage.tsx";
import ArtistReviewDetailPage from "./pages/ArtistReviewDetailPage.tsx";
import SearchPage from "./pages/SearchPage.tsx";
import RegisterPage from "./pages/RegisterPage.tsx";
import LoginPage from "./pages/LoginPage.tsx";


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
              <Route path={"artists/:id"} element={<ArtistReviewDetailPage />}/>
              <Route path="/search" element={<SearchPage />} />
              <Route path = "/register" element={<RegisterPage />} />
              <Route path = "/login" element={<LoginPage/>} />
          </Routes>
      </>
  )
}

export default App
