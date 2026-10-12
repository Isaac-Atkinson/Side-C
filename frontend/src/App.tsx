import './App.css'
import AlbumReviewsPage from "./pages/AlbumReviewsPage.tsx";
import ArtistReviewsPage from "./pages/ArtistReviewsPage.tsx";
import {Route, Routes} from "react-router-dom";
import AlbumReviewDetailPage from "./pages/AlbumReviewDetailPage.tsx";
import ArtistReviewDetailPage from "./pages/ArtistReviewDetailPage.tsx";
import SearchPage from "./pages/SearchPage.tsx";
import RegisterPage from "./pages/RegisterPage.tsx";
import LoginPage from "./pages/LoginPage.tsx";
import NavBar from "./components/NavBar.tsx";
import {Box} from "@mui/material";



function App() {

  return(
      <>
          <Box
              sx={{
                  minHeight: "100vh",
                  display: "flex",
                  flexDirection: "column",
              }}
          >
              <NavBar />

              <Box
                  component="main"
                  sx={{
                      flex: 1,
                      display: "flex",
                      flexDirection: "column",
                  }}
              >
                  <Routes>
                      <Route path="/albums" element={<AlbumReviewsPage />} />
                      <Route path="/artists" element={<ArtistReviewsPage />} />
                      <Route path={"albums/:id"} element={<AlbumReviewDetailPage />}/>
                      <Route path={"artists/:id"} element={<ArtistReviewDetailPage />}/>
                      <Route path="/search" element={<SearchPage />} />
                      <Route path = "/register" element={<RegisterPage />} />
                      <Route path = "/login" element={<LoginPage/>} />
                  </Routes>
              </Box>
          </Box>
      </>
  )
}

export default App
